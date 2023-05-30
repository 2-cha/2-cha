package com._2cha.demo.collection.service;


import com._2cha.demo.bookmark.exception.AlreadyBookmarkedException;
import com._2cha.demo.bookmark.exception.NotBookmarkedException;
import com._2cha.demo.collection.domain.Collection;
import com._2cha.demo.collection.domain.CollectionBookmark;
import com._2cha.demo.collection.dto.CollectionCreatedResponse;
import com._2cha.demo.collection.dto.CollectionRemovedResponse;
import com._2cha.demo.collection.dto.CollectionReviewsResponse;
import com._2cha.demo.collection.dto.CollectionReviewsUpdatedResponse;
import com._2cha.demo.collection.dto.CollectionUpdatedResponse;
import com._2cha.demo.collection.dto.CollectionViewResponse;
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
import com._2cha.demo.review.dto.ReviewResponse;
import com._2cha.demo.review.service.ReviewService;
import java.util.List;
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

  /*-----------
   @ Queries
   ----------*/
  @Transactional(readOnly = true)
  public List<CollectionViewResponse> getMemberCollections(Long memberId, boolean exposedOnly) {
    return collectionQueryRepository.getMemberCollections(memberId, exposedOnly,
                                                          fileStorageService.getBaseUrl());
  }

  @Transactional(readOnly = true)
  public CollectionReviewsResponse getCollectionDetail(Long memberId, Long collId) {
    Collection collection = collectionRepository.findCollectionById(collId);
    if (collection == null) throw new NoSuchCollectionException();

    List<Long> reviewIds = collection.getReviews()
                                     .stream()
                                     .map(revInColl -> revInColl.getReview().getId())
                                     .toList();
    List<ReviewResponse> reviews = reviewService.getReviewsByIdInPreservingOrder(reviewIds);
    return new CollectionReviewsResponse(collection, reviews, fileStorageService.getBaseUrl());
  }

  @Transactional(readOnly = true)
  public List<CollectionViewResponse> getBookmarkedCollections(Long memberId) {
    List<CollectionBookmark> bookmarks = bookmarkRepository.findAllByMemberId(memberId);
    List<Long> collIds = bookmarks.stream().map(b -> b.getCollection().getId()).toList();
    return collectionQueryRepository.getCollectionsByIdIn(collIds, fileStorageService.getBaseUrl());
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
