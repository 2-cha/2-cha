package com._2cha.demo.review.service;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import com._2cha.demo.global.exception.ForbiddenException;
import com._2cha.demo.global.infra.imageupload.service.ImageUploadService;
import com._2cha.demo.global.infra.storage.service.FileStorageService;
import com._2cha.demo.member.domain.Member;
import com._2cha.demo.member.dto.MemberProfileResponse;
import com._2cha.demo.member.service.MemberService;
import com._2cha.demo.place.domain.Place;
import com._2cha.demo.place.dto.PlaceBriefResponse;
import com._2cha.demo.place.service.PlaceService;
import com._2cha.demo.review.domain.Review;
import com._2cha.demo.review.domain.Tag;
import com._2cha.demo.review.dto.ReviewResponse;
import com._2cha.demo.review.dto.TagCountResponse;
import com._2cha.demo.review.repository.ReviewRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

  private static final Integer SUMMARY_SIZE = 3;
  private final ReviewRepository reviewRepository;
  private final TagService tagService;
  private final FileStorageService fileStorageService;
  private final ImageUploadService imageUploadService;

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
    Member member = memberService.findById(memberId);
    Place place = placeService.findPlaceById(placeId);

    List<String> imageUrlPaths = imageUrlList.stream()
                                             .map(fileStorageService::extractPath)
                                             .toList();

    List<String> thumbnailUrlPaths = imageUrlPaths.stream()
                                                  .map(imageUploadService::getThumbnailPath)
                                                  .toList();
    Review review = Review.createReview(place, member, tagList, imageUrlPaths, thumbnailUrlPaths);
    reviewRepository.save(review);
  }

  @Transactional
  public void deleteReview(Long memberId, Long reviewId) {
    Review review = reviewRepository.findReviewById(reviewId);
    if (review.getMember().getId() != memberId) {
      throw new ForbiddenException("Cannot delete other member's review");
    }

    reviewRepository.deleteReviewById(reviewId);
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

  public List<ReviewResponse> getReviewsByMemberId(Long memberId, Pageable pageParam) {
    List<Review> reviews = reviewRepository.findReviewsByMemberId(memberId, pageParam);
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
                                                                   p -> p
                                                                  ));
    return reviews.stream().map(review -> new ReviewResponse(review,
                                                             // set null to ignore in api response
                                                             member,
                                                             placeMap.get(
                                                                 review.getPlace().getId()),
                                                             fileStorageService.getBaseUrl()

    )).toList();
  }


  public List<ReviewResponse> getReviewsByPlaceId(Long placeId, Pageable pageParam) {
    List<Review> reviews = reviewRepository.findReviewsByPlaceId(placeId, pageParam);
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
                                                                      c -> c));

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
    Map<Tag, Integer> tagCountMap = new HashMap<>();

    reviews.forEach(review ->
                        review.getTags()
                              .forEach(
                                  tag -> tagCountMap.put(tag, tagCountMap.getOrDefault(tag, 0) + 1))
                   );

    Stream<Entry<Tag, Integer>> entryStream = tagCountMap.entrySet()
                                                         .stream();

    if (size != null) {
      entryStream = entryStream.limit(size);
    }

    return entryStream.sorted(Entry.comparingByValue())
                      .map(entry -> new TagCountResponse(entry.getKey(), entry.getValue()))
                      .toList();
  }

  public Map<Long, List<TagCountResponse>> getReviewTagCountsByPlaceIdIn(List<Long> placeIds,
                                                                         Integer tagSizeLimit) {
    List<Review> reviews = reviewRepository.findReviewsByPlaceIdIn(placeIds);

    Map<Long, Map<Tag, Integer>> placesTagCountMap = new HashMap<>();
    Map<Long, List<TagCountResponse>> placesTagCountResponseMap = new HashMap<>();

    for (Long placeId : placeIds) {
      placesTagCountMap.put(placeId, new HashMap<>());
    }

    reviews.forEach(review -> {
                      Map<Tag, Integer> tagCountMap = placesTagCountMap.get(review.getPlace()
                                                                                  .getId());
                      review.getTags()
                            .forEach(tag -> tagCountMap.put(tag, tagCountMap.getOrDefault(tag, 0) + 1));
                    }
                   );

    placesTagCountMap.forEach(
        (placeId, tagCountMap) -> {
          Stream<Entry<Tag, Integer>> tagCounts = tagCountMap.entrySet()
                                                             .stream();
          if (tagSizeLimit != null) {
            tagCounts = tagCounts.limit(tagSizeLimit);
          }

          List<TagCountResponse> tagCountResponses = tagCounts.sorted(Entry.comparingByValue())
                                                              .map(entry -> new TagCountResponse(
                                                                  entry.getKey(), entry.getValue()
                                                              ))
                                                              .toList();
          placesTagCountResponseMap.put(placeId, tagCountResponses);
        });
    return placesTagCountResponseMap;
  }
}
