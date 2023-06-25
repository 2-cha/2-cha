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
import com._2cha.demo.collection.dto.ReviewInCollectionResponse;
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
import com._2cha.demo.member.dto.MemberProfileResponse;
import com._2cha.demo.member.exception.NoSuchMemberException;
import com._2cha.demo.member.service.MemberService;
import com._2cha.demo.recommendation.service.RecommendationService;
import com._2cha.demo.review.domain.Review;
import com._2cha.demo.review.dto.LikeStatus;
import com._2cha.demo.review.dto.ReviewResponse;
import com._2cha.demo.review.service.ReviewService;
import com._2cha.demo.util.GeomUtils;
import jakarta.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
  private final RecommendationService recommendationService;

  /*-----------
   @ Queries
   ----------*/
  @Transactional(readOnly = true)
  public List<CollectionBriefResponse> getMemberCollections(@Nullable Long requesterId,
                                                            Long memberId) {

    List<CollectionBriefResponse> collections = collectionQueryRepository.getMemberCollections(
        memberId,
        // expose private only if requester is collection owner
        !Objects.equals(requesterId, memberId),
        fileStorageService.getBaseUrl());

    List<Long> collIds = collections.stream().map(CollectionBriefResponse::getId).toList();
    Map<Long, LikeStatus> likeStatus = likeService.getLikeStatus(requesterId, collIds);
    Map<Long, BookmarkStatus> bookmarkStatus = getBookmarkStatus(requesterId, collIds);

    collections.forEach(collection -> {
      collection.setLikeStatus(likeStatus.get(collection.getId()));
      collection.setBookmarkStatus(bookmarkStatus.get(collection.getId()));
    });

    return collections;
  }

  @Transactional(readOnly = true)
  public List<CollectionBriefResponse> getLatestCollections(Long memberId, Pageable pageParam) {

    List<CollectionBriefResponse> collections = collectionQueryRepository.getLatestCollections(
        fileStorageService.getBaseUrl(),
        pageParam.getOffset(),
        pageParam.getPageSize());

    List<Long> collIds = collections.stream().map(CollectionBriefResponse::getId).toList();
    Map<Long, LikeStatus> likeStatus = likeService.getLikeStatus(memberId, collIds);
    Map<Long, BookmarkStatus> bookmarkStatus = getBookmarkStatus(memberId, collIds);

    collections.forEach(collection -> {
      collection.setLikeStatus(likeStatus.get(collection.getId()));
      collection.setBookmarkStatus(bookmarkStatus.get(collection.getId()));
    });

    return collections;
  }

  @Transactional(readOnly = true)
  public List<CollectionBriefResponse> getNearbyCollections(Double lat, Double lon,
                                                            Double distance) {
    final double RATIO_THRESHOLD = 0.5;
    Map<Long, Long> nearbyPlaceCount = collectionQueryRepository.getNearbyPlaceCount(
        GeomUtils.createPoint(lat, lon), distance);
    List<Long> collIds = new ArrayList<>(nearbyPlaceCount.keySet());
    Map<Long, Long> totalPlaceCount = collectionQueryRepository.getPlaceCount(collIds);

    collIds.removeIf(collId -> {
      double nearbyCount = nearbyPlaceCount.get(collId);
      double totalCount = totalPlaceCount.get(collId);
      return ((nearbyCount / totalCount) < RATIO_THRESHOLD);
    });

    return collectionQueryRepository.getCollectionsByIdIn(collIds, fileStorageService.getBaseUrl());
  }

  @Transactional(readOnly = true)
  public List<CollectionBriefResponse> getRecommendations(Long memberId, Double lat, Double lon,
                                                          Double distance) {
    final int RECOMMENDATION_SIZE = 10;

    Set<Long> collIds = new HashSet<>();
    getBookmarkedCollections(memberId).forEach(coll -> collIds.add(coll.getId()));
    getLikedCollections(memberId).forEach(coll -> collIds.add(coll.getId()));

    List<Long> recommended = recommendationService.recommend(collIds.stream().toList(),
                                                             RECOMMENDATION_SIZE)
                                                  .stream()
                                                  .map(RecommendedItem::getItemID)
                                                  .toList();

    List<CollectionBriefResponse> result = new ArrayList<>(
        collectionQueryRepository.getCollectionsByIdIn(recommended,
                                                       fileStorageService.getBaseUrl()));

    // fill up with nearby / latest collections
    if (result.size() < RECOMMENDATION_SIZE) {
      // add to result if not already recommended
      getNearbyCollections(lat, lon, distance).stream()
                                              .filter(Predicate.not(result::contains))
                                              .limit(RECOMMENDATION_SIZE - result.size())
                                              .forEach(result::add);
    }
    if (result.size() < RECOMMENDATION_SIZE) {
      // add to result if not already recommended
      getLatestCollections(memberId, Pageable.ofSize(RECOMMENDATION_SIZE - recommended.size()))
          .stream()
          .filter(Predicate.not(result::contains))
          .forEach(result::add);
    }
    return result;
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
    List<ReviewResponse> reviewResponses = reviewService.getReviewsByIdInPreservingOrder(reviewIds);
    List<ReviewInCollectionResponse> revInCollResponses = reviewResponses.stream()
                                                                         .map(
                                                                             review -> new ReviewInCollectionResponse(
                                                                                 review.getId(),
                                                                                 review.getImages()
                                                                                       .get(0),
                                                                                 review.getPlace(),
                                                                                 review.getTags()))
                                                                         .toList();
    MemberProfileResponse member = memberService.getMemberProfileById(
        collection.getMember().getId());

    CollectionDetailResponse detail = new CollectionDetailResponse(collection,
                                                                   member,
                                                                   revInCollResponses,
                                                                   fileStorageService.getBaseUrl());
    detail.setBookmarkStatus(getBookmarkStatus(memberId, collId));
    detail.setLikeStatus(likeService.getLikeStatus(memberId, collId));
    return detail;
  }

  @Transactional(readOnly = true)
  public List<CollectionBriefResponse> getBookmarkedCollections(Long memberId) {
    List<CollectionBookmark> bookmarks = bookmarkRepository.findAllByMemberId(memberId);
    List<Long> collIds = bookmarks.stream().map(b -> b.getCollection().getId()).toList();
    return collectionQueryRepository.getCollectionsByIdIn(collIds, fileStorageService.getBaseUrl());
  }

  @Transactional(readOnly = true)
  public List<CollectionBriefResponse> getLikedCollections(Long memberId) {
    return collectionQueryRepository.getCollectionsByIdIn(likeService.getLikedCollections(memberId),
                                                          fileStorageService.getBaseUrl());
  }

  @Transactional(readOnly = true)
  public Map<Long, BookmarkStatus> getBookmarkStatus(Long memberId, List<Long> collIds) {
    List<CollectionBookmark> bookmarks =
        (memberId != null) ? bookmarkRepository.findAllByMemberIdAndCollectionIdIn(memberId,
                                                                                   collIds)
                           : Collections.emptyList();
    List<Long> bookmarkedIds = bookmarks.stream().map(b -> b.getCollection().getId()).toList();
    Map<Long, Long> totalCountMap = bookmarkRepository.countAllByCollectionIdIn(collIds).stream()
                                                      .collect(toMap(
                                                          BookmarkCountProjection::getId,
                                                          BookmarkCountProjection::getCount));
    return collIds.stream()
                  .collect(toMap(id -> id,
                                 id -> new BookmarkStatus(bookmarkedIds.contains(id),
                                                          totalCountMap.getOrDefault(id, 0L))));
  }

  @Transactional(readOnly = true)
  public BookmarkStatus getBookmarkStatus(@Nullable Long memberId, Long collId) {
    CollectionBookmark bookmark =
        (memberId != null) ? bookmarkRepository.findByMemberIdAndCollectionId(memberId,
                                                                              collId)
                           : null;
    Long count = bookmarkRepository.countAllByCollectionId(collId);
    return new BookmarkStatus(bookmark != null, count);
  }

  @Transactional(readOnly = true)
  public Collection findById(Long collId) {
    Collection collection = collectionRepository.findCollectionById(collId);
    if (collection == null) throw new NoSuchCollectionException();

    return collection;
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
