package com._2cha.demo.review.service;

import com._2cha.demo.global.exception.ForbiddenException;
import com._2cha.demo.member.domain.Member;
import com._2cha.demo.member.service.MemberService;
import com._2cha.demo.place.domain.Place;
import com._2cha.demo.place.dto.PlaceBriefResponse;
import com._2cha.demo.place.service.PlaceService;
import com._2cha.demo.review.domain.Review;
import com._2cha.demo.review.domain.Tag;
import com._2cha.demo.review.dto.MemberReviewResponse;
import com._2cha.demo.review.dto.PlaceReviewResponse;
import com._2cha.demo.review.dto.TagCountResponse;
import com._2cha.demo.review.repository.ReviewRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

  private final Integer SUMMARY_SIZE = 3;
  private final ReviewRepository reviewRepository;
  private final TagService tagService;

  private MemberService memberService;
  private PlaceService placeService;


  @Autowired
  public void setMemberService(MemberService memberService) {
    this.memberService = memberService;
  }

  @Autowired
  public void setPlaceService(PlaceService placeService) {
    this.placeService = placeService;
  }

  public List<Review> findReviewsByIdIn(List<Long> ids) {
    return reviewRepository.findReviewsByIdIn(ids);
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

    Review review = Review.createReview(place, member, tagList, imageUrlList);
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

  public List<MemberReviewResponse> getReviewsByMemberId(Long memberId) {
    List<Review> reviews = reviewRepository.findReviewsByMemberId(memberId);
    if (reviews.isEmpty()) {
      return new ArrayList<>();
    }
    List<MemberReviewResponse> dtos = new ArrayList<>();
    Set<Long> placeIds = new TreeSet<>();
    Map<Review, Long> reviewWithPlaceIdMap = new HashMap<>();

    reviews.forEach(
        review -> {
          Long placeId = review.getPlace()
                               .getId();
          placeIds.add(placeId);
          reviewWithPlaceIdMap.put(review, placeId);
        });
    List<PlaceBriefResponse> placesBrief = placeService.getPlacesBriefByIdIn(placeIds.stream()
                                                                                     .toList(),
                                                                             SUMMARY_SIZE);

    Map<Long, PlaceBriefResponse> placesBriefWithIdMap = new HashMap<>();
    for (var placeBrief : placesBrief) {
      placesBriefWithIdMap.put(placeBrief.getId(), placeBrief);
    }

    reviewWithPlaceIdMap.forEach((review, placeId) -> {
      MemberReviewResponse dto = new MemberReviewResponse(review);
      dto.setPlace(placesBriefWithIdMap.get(placeId));
      dtos.add(dto);
    });
    return dtos;
  }


  public List<PlaceReviewResponse> getReviewsByPlaceId(Long placeId) {
    List<Review> reviews = reviewRepository.findReviewsByPlaceId(placeId);
    List<PlaceReviewResponse> dtos = new ArrayList<>();

    reviews.forEach(
        review -> {
          Member member = review.getMember();
          PlaceReviewResponse dto = new PlaceReviewResponse(review);
          //TODO: In Operator
          dto.setMember(memberService.getMemberProfileById(member.getId()));
          dtos.add(dto);
        });

    return dtos;
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
