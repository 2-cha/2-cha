package com._2cha.demo.review.service;

import com._2cha.demo.global.exception.UnauthorizedException;
import com._2cha.demo.member.domain.Member;
import com._2cha.demo.member.service.MemberService;
import com._2cha.demo.place.domain.Place;
import com._2cha.demo.place.service.PlaceService;
import com._2cha.demo.review.domain.Review;
import com._2cha.demo.review.domain.Tag;
import com._2cha.demo.review.dto.MemberReviewResponse;
import com._2cha.demo.review.dto.PlaceReviewResponse;
import com._2cha.demo.review.dto.TagCountResponse;
import com._2cha.demo.review.repository.ReviewRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
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

  public List<MemberReviewResponse> getReviewsByMemberId(Long memberId) {
    List<Review> reviews = reviewRepository.findReviewsByMemberId(memberId);
    List<MemberReviewResponse> dtos = new ArrayList<>();

    reviews.forEach(
        review -> {
          MemberReviewResponse dto = new MemberReviewResponse();
          Place place = review.getPlace();
          dto.setPlace(placeService.getPlaceBriefById(place.getId(), SUMMARY_SIZE));
          dto.fetchTagsAndImagesFromReview(review);
          dtos.add(dto);
        });

    return dtos;
  }


  public List<PlaceReviewResponse> getReviewsByPlaceId(Long placeId) {
    List<Review> reviews = reviewRepository.findReviewsByPlaceId(placeId);
    List<PlaceReviewResponse> dtos = new ArrayList<>();

    reviews.forEach(
        review -> {
          PlaceReviewResponse dto = new PlaceReviewResponse();
          Member member = review.getMember();
          dto.setMember(memberService.getMemberProfileById(member.getId()));
          dto.fetchTagsAndImagesFromReview(review);
          dtos.add(dto);
        });

    return dtos;
  }

  public List<TagCountResponse> getReviewTagCountByPlaceId(Long placeId, Integer size) {
    List<Review> reviews = reviewRepository.findReviewsByPlaceId(placeId);
    Map<Tag, Integer> tagCountMap = new ConcurrentHashMap<>();

    reviews.forEach(review ->
                        review.getTags()
                              .stream()
                              .forEach(
                                  tag -> tagCountMap.put(tag, tagCountMap.getOrDefault(tag, 0) + 1))
                   );

    Stream<Entry<Tag, Integer>> sorted = tagCountMap.entrySet()
                                                    .stream()
                                                    .sorted(Entry.comparingByValue());
    if (size != null) {
      sorted.limit(size);
    }
    return sorted.map(entry -> {
                   TagCountResponse dto = new TagCountResponse();
                   Tag tag = entry.getKey();
                   dto.setId(tag.getId());
                   dto.setEmoji(tag.getEmoji());
                   dto.setMessage(tag.getMsg());
                   dto.setCount(entry.getValue());
                   return dto;
                 })
                 .toList();
  }

  public void writeReview(Long memberId, Long placeId,
                          List<Long> tagIdList, List<String> imageUrlList) {

    List<Tag> tagList = tagIdList.stream()
                                 .map(tagId -> tagService.getTagById(tagId))
                                 .toList();
    Member member = memberService.getMemberById(memberId);
    Place place = placeService.getPlaceById(placeId);

    Review.createReview(place, member, tagList, imageUrlList);
  }

  public void deleteReview(Long memberId, Long reviewId) {
    Review review = reviewRepository.findReviewById(reviewId);
    if (review.getMember().getId() != memberId) {
      throw new UnauthorizedException("Cannot delete other member's review");
    }

    reviewRepository.deleteReviewById(reviewId);
  }
}
