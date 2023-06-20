package com._2cha.demo.collection.service;


import static java.util.stream.Collectors.toMap;

import com._2cha.demo.bookmark.dto.BookmarkCountProjection;
import com._2cha.demo.bookmark.dto.BookmarkStatus;
import com._2cha.demo.bookmark.exception.AlreadyBookmarkedException;
import com._2cha.demo.bookmark.exception.NotBookmarkedException;
import com._2cha.demo.collection.domain.Collection;
import com._2cha.demo.collection.domain.CollectionBookmark;
import com._2cha.demo.collection.dto.CollectionBriefResponse;
import com._2cha.demo.collection.dto.CollectionCreatedResponse;
import com._2cha.demo.collection.dto.CollectionDetailResponse;
import com._2cha.demo.collection.dto.CollectionRemovedResponse;
import com._2cha.demo.collection.dto.CollectionReviewsUpdatedResponse;
import com._2cha.demo.collection.dto.CollectionUpdatedResponse;
import com._2cha.demo.collection.exception.CannotAccessToPrivateException;
import com._2cha.demo.collection.exception.CannotCreateException;
import com._2cha.demo.collection.exception.CannotRemoveException;
import com._2cha.demo.collection.exception.CannotUpdateException;
import com._2cha.demo.collection.exception.InvalidReviewIdIncludedException;
import com._2cha.demo.collection.exception.NoSuchCollectionException;
import com._2cha.demo.collection.repository.CollectionBookmarkRepository;
import com._2cha.demo.collection.repository.CollectionQueryRepository;
import com._2cha.demo.collection.repository.CollectionRepository;
import com._2cha.demo.collection.repository.ReviewInCollectionRepository;
import com._2cha.demo.global.infra.storage.service.FileStorageService;
import com._2cha.demo.member.domain.Member;
import com._2cha.demo.member.exception.NoSuchMemberException;
import com._2cha.demo.member.service.MemberService;
import com._2cha.demo.review.domain.Review;
import com._2cha.demo.review.dto.LikeStatus;
import com._2cha.demo.review.dto.ReviewResponse;
import com._2cha.demo.review.service.ReviewService;
import jakarta.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CollectionService {

  private final CollectionRepository collectionRepository;
  private final ReviewInCollectionRepository revInCollRepository;
  private final CollectionBookmarkRepository bookmarkRepository;
  private final CollectionQueryRepository collectionQueryRepository;
  private final FileStorageService fileStorageService;
  private final MemberService memberService;
  private final ReviewService reviewService;
  private final CollectionLikeService likeService;

  /*-----------
   @ Queries
   ----------*/
  @Transactional(readOnly = true)
  public List<CollectionBriefResponse> getMemberCollections(Long memberId, boolean exposedOnly) {
    return collectionQueryRepository.getMemberCollections(memberId, exposedOnly,
                                                          fileStorageService.getBaseUrl());
  }

  @Transactional(readOnly = true)
  public CollectionDetailResponse getCollectionDetail(Long memberId, Long collId) {
    Collection collection = collectionRepository.findCollectionById(collId);
    if (collection == null) throw new NoSuchCollectionException();
    if (!collection.isExposed() &&
        !Objects.equals(memberId, collection.getMember().getId())) {
      throw new CannotAccessToPrivateException();
    }

    List<Long> reviewIds = collection.getReviews()
                                     .stream()
                                     .map(revInColl -> revInColl.getReview().getId())
                                     .toList();
    List<ReviewResponse> reviews = reviewService.getReviewsByIdInPreservingOrder(reviewIds);
    return new CollectionDetailResponse(collection, reviews, fileStorageService.getBaseUrl());
  }

  @Transactional(readOnly = true)
  public List<CollectionBriefResponse> getBookmarkedCollections(Long memberId) {
    List<CollectionBookmark> bookmarks = bookmarkRepository.findAllByMemberId(memberId);
    List<Long> collIds = bookmarks.stream().map(b -> b.getCollection().getId()).toList();
    return collectionQueryRepository.getCollectionsByIdIn(collIds, fileStorageService.getBaseUrl());
  }

  @Transactional(readOnly = true)
  public void setResponseBookmarkStatus(Long memberId, List<CollectionBriefResponse> collections) {
    List<Long> collIds = collections.stream().map(CollectionBriefResponse::getId).toList();
    List<CollectionBookmark> bookmarks =
        (memberId != null) ? bookmarkRepository.findAllByMemberIdAndCollectionIdIn(memberId,
                                                                                   collIds)
                           : Collections.emptyList();
    List<Long> bookmarkedIds = bookmarks.stream().map(b -> b.getCollection().getId()).toList();
    Map<Long, Long> totalCountMap = bookmarkRepository.countAllByCollectionIdIn(collIds).stream()
                                                      .collect(toMap(
                                                          BookmarkCountProjection::getId,
                                                          BookmarkCountProjection::getCount));
    for (var collection : collections) {
      collection.setBookmarkStatus(new BookmarkStatus(bookmarkedIds.contains(collection.getId()),
                                                      totalCountMap.getOrDefault(collection.getId(),
                                                                                 0L)));
    }
  }

  @Transactional(readOnly = true)
  public void setResponseLikeStatus(@Nullable Long memberId,
                                    CollectionBriefResponse collection) {
    collection.setLikeStatus(likeService.getLikeStatus(memberId, collection.getId()));
  }

  @Transactional(readOnly = true)
  public void setResponseLikeStatus(Long memberId, List<CollectionBriefResponse> collections) {
    Map<Long, LikeStatus> likeStatusMap = likeService.getLikeStatus(memberId,
                                                                    collections.stream().map(
                                                                                   CollectionBriefResponse::getId)
                                                                               .toList());

    collections.forEach(
        collection -> collection.setLikeStatus(likeStatusMap.get(collection.getId())));
  }

  @Transactional(readOnly = true)
  public void setResponseBookmarkStatus(@Nullable Long memberId,
                                        CollectionBriefResponse collection) {
    CollectionBookmark bookmark =
        (memberId != null) ? bookmarkRepository.findByMemberIdAndCollectionId(memberId,
                                                                              collection.getId())
                           : null;
    Long count = bookmarkRepository.countAllByCollectionId(collection.getId());
    collection.setBookmarkStatus(new BookmarkStatus(bookmark != null, count));
  }


  /*-----------
   @ Commands
   ----------*/
  public CollectionCreatedResponse createCollection(Long memberId, String title,
                                                    String description, String thumbnailUrl,
                                                    List<Long> reviewIds) {
    Member member = memberService.findById(memberId);
    List<Review> reviews = reviewService.findReviewsByIdInPreservingOrder(reviewIds);
    if (reviews.size() != reviewIds.size()) throw new InvalidReviewIdIncludedException();
    for (Review review : reviews) {
      if (!Objects.equals(review.getMember().getId(), memberId)) throw new CannotCreateException();
    }

    Collection collection = Collection.createCollection(member,
                                                        title,
                                                        description,
                                                        fileStorageService.extractPath(
                                                            thumbnailUrl),
                                                        reviews);
    collectionRepository.save(collection);

    return new CollectionCreatedResponse(collection);
  }

  public CollectionRemovedResponse removeCollection(Long memberId, Long collId) {
    Collection collection = collectionRepository.findCollectionById(collId);
    if (collection == null) throw new NoSuchCollectionException();
    if (!Objects.equals(collection.getMember().getId(), memberId)) {
      throw new CannotRemoveException();
    }

    collectionRepository.deleteCollectionById(collection.getId());
    return new CollectionRemovedResponse(); //TODO: DTO
  }

  public CollectionUpdatedResponse updateCollection(Long memberId, Long collId, String title,
                                                    String description, String thumbnailUrl,
                                                    Boolean exposure) {
    Collection collection = collectionRepository.findCollectionById(collId);
    if (collection == null) throw new NoSuchCollectionException();
    if (!Objects.equals(collection.getMember().getId(), memberId)) {
      throw new CannotUpdateException();
    }

    // for PATCH method
    if (title != null) collection.updateTitle(title);
    if (description != null) collection.updateDescription(description);
    if (thumbnailUrl != null) {
      collection.updateThumbnailUrlPath(fileStorageService.extractPath(thumbnailUrl));
    }
    if (exposure != null) collection.toggleExposure(exposure);

    return new CollectionUpdatedResponse(collection, fileStorageService.getBaseUrl());
  }

  public CollectionReviewsUpdatedResponse updateReviews(Long memberId, Long collId,
                                                        List<Long> reviewIds) {
    Collection collection = collectionRepository.findCollectionById(collId);
    if (collection == null) throw new NoSuchCollectionException();
    if (!Objects.equals(collection.getMember().getId(), memberId)) {
      throw new CannotUpdateException();
    }
    List<Review> reviews = reviewService.findReviewsByIdInPreservingOrder(reviewIds);
    if (reviews.size() != reviewIds.size()) throw new InvalidReviewIdIncludedException();
    for (Review review : reviews) {
      if (!Objects.equals(review.getMember().getId(), memberId)) throw new CannotCreateException();
    }

    collection.updateReviews(reviews);

    return new CollectionReviewsUpdatedResponse(collection);
  }

  @Transactional
  public void createBookmark(Long memberId, Long collId) {
    Member member = memberService.findById(memberId);
    if (member == null) throw new NoSuchMemberException();

    Collection collection = collectionRepository.findCollectionById(collId);
    if (collection == null) throw new NoSuchCollectionException();

    if (bookmarkRepository.findByMemberIdAndCollectionId(memberId, collId) != null) {
      throw new AlreadyBookmarkedException();
    }

    CollectionBookmark bookmark = new CollectionBookmark(member, collection);
    bookmarkRepository.save(bookmark);
  }

  @Transactional
  public void removeBookmark(Long memberId, Long collId) {
    CollectionBookmark bookmark = bookmarkRepository.findByMemberIdAndCollectionId(memberId,
                                                                                   collId);
    if (bookmark == null) {
      throw new NotBookmarkedException();
    }

    bookmarkRepository.delete(bookmark);
  }
}
