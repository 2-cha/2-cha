package com._2cha.demo.review.service;

import static java.util.Comparator.reverseOrder;
import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static net.logstash.logback.marker.Markers.append;

import com._2cha.demo.bookmark.dto.BookmarkCountProjection;
import com._2cha.demo.bookmark.dto.BookmarkStatus;
import com._2cha.demo.bookmark.exception.AlreadyBookmarkedException;
import com._2cha.demo.bookmark.exception.NotBookmarkedException;
import com._2cha.demo.global.event.FirstReviewCreatedEvent;
import com._2cha.demo.global.infra.imageupload.service.ImageUploadService;
import com._2cha.demo.global.infra.storage.service.FileStorageService;
import com._2cha.demo.member.domain.Member;
import com._2cha.demo.member.dto.MemberProfileResponse;
import com._2cha.demo.member.exception.NoSuchMemberException;
import com._2cha.demo.member.service.MemberService;
import com._2cha.demo.place.domain.Place;
import com._2cha.demo.place.dto.NearbyPlaceSearchParams;
import com._2cha.demo.place.dto.PlaceBriefResponse;
import com._2cha.demo.place.dto.PlaceBriefWithDistanceResponse;
import com._2cha.demo.place.exception.NoSuchPlaceException;
import com._2cha.demo.place.service.PlaceService;
import com._2cha.demo.review.domain.Review;
import com._2cha.demo.review.domain.ReviewBookmark;
import com._2cha.demo.review.domain.Tag;
import com._2cha.demo.review.dto.ReviewResponse;
import com._2cha.demo.review.dto.TagCountResponse;
import com._2cha.demo.review.exception.CannotRemoveException;
import com._2cha.demo.review.exception.InvalidTagsException;
import com._2cha.demo.review.exception.NoSuchReviewException;
import com._2cha.demo.review.repository.ReviewBookmarkRepository;
import com._2cha.demo.review.repository.ReviewRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

  private static final Integer SUMMARY_SIZE = 3;
  private static final Logger reviewTagsLogger = LoggerFactory.getLogger("ReviewTagsLogger");

  private final ReviewRepository reviewRepository;
  private final ReviewBookmarkRepository reviewBookmarkRepository;
  private final TagService tagService;
  private final FileStorageService fileStorageService;
  private final ImageUploadService imageUploadService;
  private final ApplicationEventPublisher eventPublisher;

  private MemberService memberService;
  private PlaceService placeService;


  @Autowired
  public void setMemberService(MemberService memberService) {
    this.memberService = memberService;
  }

  @Lazy
  @Autowired
  public void setPlaceService(PlaceService placeService) {
    this.placeService = placeService;
  }

  /*-----------
   @ Commands
   ----------*/

  @Transactional
  public void writeReview(Long memberId, Long placeId,
                          List<Long> tagIdList, List<String> imageUrlList) {

    List<Tag> tagList = tagService.findTagsByIdIn(tagIdList);
    if (tagList.isEmpty()) throw new InvalidTagsException();

    Member member = memberService.findById(memberId);
    if (member == null) throw new NoSuchMemberException();

    Place place = placeService.findPlaceById(placeId);
    if (place == null) throw new NoSuchPlaceException();

    List<String> imageUrlPaths = imageUrlList.stream()
                                             .map(fileStorageService::extractPath)
                                             .toList();

    List<String> thumbnailUrlPaths = imageUrlPaths.stream()
                                                  .map(imageUploadService::getThumbnailPath)
                                                  .toList();
    if (reviewRepository.findReviewsByPlaceId(placeId).isEmpty() && !imageUrlPaths.isEmpty()) {
      eventPublisher.publishEvent(new FirstReviewCreatedEvent(this, placeId,
                                                              imageUrlPaths.get(0),
                                                              thumbnailUrlPaths.get(0)));
    }
    Review review = Review.createReview(place, member, tagList, imageUrlPaths, thumbnailUrlPaths);

    reviewRepository.save(review);
    reviewTagsLogger.info(append("tag_messages", tagList.stream().map(Tag::getMsg).toList())
                              .and(append("tag_emojis",
                                          tagList.stream().map(Tag::getEmoji).toList())),
                          "");
  }

  @Transactional
  public void deleteReview(Long memberId, Long reviewId) {
    Review review = reviewRepository.findReviewById(reviewId);
    if (review == null) throw new NoSuchReviewException(reviewId);
    if (!Objects.equals(review.getMember().getId(), memberId)) throw new CannotRemoveException();

    reviewRepository.deleteReviewById(reviewId);
  }

  @Transactional
  public void createBookmark(Long memberId, Long reviewId) {
    Member member = memberService.findById(memberId);
    if (member == null) throw new NoSuchMemberException();

    Review review = reviewRepository.findReviewById(reviewId);
    if (review == null) throw new NoSuchReviewException(reviewId);

    if (reviewBookmarkRepository.findByMemberIdAndReviewId(memberId, reviewId) != null) {
      throw new AlreadyBookmarkedException();
    }

    ReviewBookmark bookmark = new ReviewBookmark(member, review);
    reviewBookmarkRepository.save(bookmark);
  }

  @Transactional
  public void removeBookmark(Long memberId, Long reviewId) {
    ReviewBookmark bookmark = reviewBookmarkRepository.findByMemberIdAndReviewId(memberId,
                                                                                 reviewId);
    if (bookmark == null) {
      throw new NotBookmarkedException();
    }

    reviewBookmarkRepository.delete(bookmark);
  }

  /*-----------
   @ Queries
   ----------*/

  public List<Review> findReviewsByIdInPreservingOrder(List<Long> ids) {
    List<Review> reviews = reviewRepository.findReviewsByIdIn(ids);
    // keep same order as requested
    return getOrderedReviews(ids, reviews);
  }

  private List<Review> getOrderedReviews(List<Long> reviewIds, List<Review> reviews) {
    Map<Long, Review> reviewMap = reviews.stream().collect(Collectors.toMap(Review::getId, r -> r));
    List<Review> orderedReviews = new ArrayList<>(reviews.size());
    for (Long id : reviewIds) {
      Review review = reviewMap.get(id);
      if (review != null) {orderedReviews.add(review);}
    }
    return orderedReviews;
  }

  public List<ReviewResponse> getReviewsByIdInPreservingOrder(List<Long> reviewIds) {
    List<Review> reviews = this.findReviewsByIdInPreservingOrder(reviewIds);
    if (reviews.isEmpty()) {
      return new ArrayList<>();
    }

    return getReviewResponses(reviews);
  }

  public List<ReviewResponse> getReviewsByMemberId(Long memberId, Pageable pageParam) {
    List<Review> reviews = reviewRepository.findReviewsByMemberIdOrderByCreatedDesc(memberId,
                                                                                    pageParam);
    if (reviews.isEmpty()) {
      return new ArrayList<>();
    }

    MemberProfileResponse member = memberService.getMemberProfileById(memberId);

    Set<Long> placeIds = reviews.stream().map(review -> review.getPlace().getId()).collect(toSet());
    Map<Long, PlaceBriefResponse> placeMap = placeService.getPlacesBriefByIdIn(
                                                             placeIds.stream().toList(),
                                                             SUMMARY_SIZE)
                                                         .stream()
                                                         .collect(
                                                             toMap(PlaceBriefResponse::getId,
                                                                   pb -> pb));
    return reviews.stream().map(review -> new ReviewResponse(review,
                                                             // set null to ignore in api response
                                                             member,
                                                             placeMap.get(
                                                                 review.getPlace().getId()),
                                                             fileStorageService.getBaseUrl()

    )).toList();
  }


  public List<ReviewResponse> getReviewsByPlaceId(Long placeId, Pageable pageParam) {
    List<Review> reviews = reviewRepository.findReviewsByPlaceIdOrderByCreatedDesc(placeId,
                                                                                   pageParam);
    if (reviews.isEmpty()) {
      return new ArrayList<>();
    }

    PlaceBriefResponse place = placeService.getPlaceBriefById(placeId, SUMMARY_SIZE);

    Set<Long> memberIds = reviews.stream()
                                 .map(review -> review.getMember().getId())
                                 .collect(toSet());
    Map<Long, MemberProfileResponse> memberMap = memberService.getMemberProfileByIdIn(
                                                                  memberIds.stream().toList())
                                                              .stream()
                                                              .collect(
                                                                  toMap(
                                                                      MemberProfileResponse::getId,
                                                                      mp -> mp));

    return reviews.stream()
                  .map(review -> new ReviewResponse(review,
                                                    memberMap.get(review.getMember().getId()),
                                                    // set null to ignore in api response
                                                    place,
                                                    fileStorageService.getBaseUrl()))
                  .toList();
  }

  public List<TagCountResponse> getReviewTagCountByPlaceId(Long placeId, Integer size) {
    List<Review> reviews = reviewRepository.findReviewsByPlaceId(placeId);
    if (reviews.isEmpty()) return new ArrayList<>();
    return calcAndSortTagCount(reviews, size);
  }

  public Map<Long, List<TagCountResponse>> getReviewTagCountsByPlaceIdIn(List<Long> placeIds,
                                                                         Integer tagSizeLimit) {
    List<Review> allReviews = reviewRepository.findReviewsByPlaceIdIn(placeIds);
    if (allReviews.isEmpty()) return new HashMap<>();

    Map<Long, List<Review>> placeReviews = allReviews.stream()
                                                     .collect(groupingBy(review -> review.getPlace()
                                                                                         .getId()));
    Map<Long, List<TagCountResponse>> placesTagCountMap = new HashMap<>();
    placeReviews.forEach((placeId, reviews) -> {
      List<TagCountResponse> placeTagCount = calcAndSortTagCount(reviews, tagSizeLimit);
      placesTagCountMap.put(placeId, placeTagCount);
    });

    return placesTagCountMap;
  }

  private List<TagCountResponse> calcAndSortTagCount(List<Review> reviews, Integer size) {
    Map<Tag, Integer> tagCountMap = new HashMap<>();
    reviews.forEach(review -> review.getTags()
                                    .forEach(tag ->
                                                 tagCountMap.put(tag,
                                                                 tagCountMap
                                                                     .getOrDefault(tag, 0) + 1)));

    Stream<TagCountResponse> tagCountStream = tagCountMap.entrySet()
                                                         .stream()
                                                         .sorted(comparingByValue(reverseOrder()))
                                                         .map(entry -> new TagCountResponse(
                                                             entry.getKey(), entry.getValue()));

    return (size == null) ? tagCountStream.toList() : tagCountStream.limit(size).toList();
  }

  public ReviewResponse getReviewById(Long reviewId) {
    Review review = reviewRepository.findReviewById(reviewId);
    if (review == null) throw new NoSuchReviewException(reviewId);

    MemberProfileResponse member = memberService.getMemberProfileById(review.getMember().getId());
    PlaceBriefResponse place = placeService.getPlaceBriefById(review.getPlace().getId(),
                                                              SUMMARY_SIZE);

    return new ReviewResponse(review, member, place, fileStorageService.getBaseUrl());
  }

  public List<ReviewResponse> getBookmarkedReviews(Long memberId) {
    List<ReviewBookmark> bookmarks = reviewBookmarkRepository.findAllByMemberId(memberId);
    List<Long> reviewIds = bookmarks.stream().map(b -> b.getReview().getId()).toList();
    return getReviewsByIdInPreservingOrder(reviewIds);
  }

  public void setResponseBookmarkStatus(Long memberId, List<ReviewResponse> reviews) {
    List<Long> reviewIds = reviews.stream().map(ReviewResponse::getId).toList();
    List<ReviewBookmark> bookmarks = reviewBookmarkRepository.findAllByMemberIdAndReviewIdIn(
        memberId, reviewIds);
    List<Long> bookmarkedIds = bookmarks.stream().map(b -> b.getReview().getId()).toList();
    Map<Long, Long> totalCountMap = reviewBookmarkRepository.countAllByReviewIdIn(reviewIds)
                                                            .stream()
                                                            .collect(toMap(
                                                                BookmarkCountProjection::getId,
                                                                BookmarkCountProjection::getCount));

    for (var review : reviews) {
      review.setBookmarkStatus(new BookmarkStatus(bookmarkedIds.contains(review.getId()),
                                                  totalCountMap.getOrDefault(review.getId(), 0L)));
    }
  }

  public void setResponseBookmarkStatus(Long memberId, ReviewResponse review) {
    ReviewBookmark bookmark = reviewBookmarkRepository.findByMemberIdAndReviewId(memberId,
                                                                                 review.getId());
    Long count = reviewBookmarkRepository.countAllByReviewId(review.getId());
    review.setBookmarkStatus(new BookmarkStatus(bookmark != null, count));
  }

  // 생성순 으로 정렬된 리뷰 목록 조회
  public List<ReviewResponse> getReviewsOrderByNewest() {

    List<Review> reviews = reviewRepository.findAllByOrderByCreatedDesc();

    if (reviews.isEmpty()) {
      return new ArrayList<>();
    }

    return getReviewResponses(reviews);
  }

  private List<ReviewResponse> getReviewResponses(List<Review> reviews) {
    Set<Long> placeIds = reviews.stream().map(review -> review.getPlace().getId()).collect(toSet());
    Set<Long> memberIds = reviews.stream().map(review -> review.getMember().getId())
                                 .collect(toSet());

    Map<Long, PlaceBriefResponse> placeMap = placeService.getPlacesBriefByIdIn(
                                                             placeIds.stream().toList(),
                                                             SUMMARY_SIZE)
                                                         .stream()
                                                         .collect(
                                                             toMap(PlaceBriefResponse::getId,
                                                                   p -> p
                                                                  ));
    Map<Long, MemberProfileResponse> memberMap = memberService.getMemberProfileByIdIn(
                                                                  memberIds.stream().toList())
                                                              .stream()
                                                              .collect(
                                                                  toMap(
                                                                      MemberProfileResponse::getId,
                                                                      m -> m));

    return reviews.stream().map(review -> new ReviewResponse(review,
                                                             memberMap.get(
                                                                 review.getMember().getId()),
                                                             placeMap.get(
                                                                 review.getPlace().getId()),
                                                             fileStorageService.getBaseUrl()
    )).toList();
  }


  //  특정 태그들을 가진 리뷰 조회
  public List<ReviewResponse> getReviewsWithTag(List<Long> filterTagsId) {

    List<Tag> filterTags = tagService.findTagsByIdIn(filterTagsId);
    List<Review> reviews = reviewRepository.findReviewsByTagsInReviewTagIn(filterTags);
    if (reviews.isEmpty()) {
      return new ArrayList<>();
    }

    return getReviewResponses(reviews);
  }

  public List<ReviewResponse> getReviewsOfNearbyPlaces(NearbyPlaceSearchParams nearbyPlacesParams,
                                                       List<Long> filterTagsId) {
    //1. 주변 가게들 조회
    //2. 가게들의 리뷰들 조회
    //3. 리뷰들을 리뷰응답으로 변환
    //4. 리뷰응답들을 리뷰응답리스트로 변환
    //5. 리뷰응답리스트 반환
    List<Tag> filterTags = tagService.findTagsByIdIn(filterTagsId);
    List<PlaceBriefWithDistanceResponse> nearbyPlaces = placeService.getNearbyPlacesBriefWithDistance(
        nearbyPlacesParams);
    if (nearbyPlaces.isEmpty()) {
      return new ArrayList<>();
    }
    List<Review> reviews = reviewRepository.findReviewByPlaceIdInAndTagsInReviewTagIdIn(
        nearbyPlaces.stream().map(PlaceBriefWithDistanceResponse::getId).toList(), filterTagsId);
    if (reviews.isEmpty()) {
      return new ArrayList<>();
    }
    return getReviewResponses(reviews);
  }
}
