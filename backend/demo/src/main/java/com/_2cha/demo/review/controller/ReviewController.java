package com._2cha.demo.review.controller;

import com._2cha.demo.global.annotation.Auth;
import com._2cha.demo.global.annotation.Authed;
import com._2cha.demo.review.dto.ReviewResponse;
import com._2cha.demo.review.dto.WriteReviewRequest;
import com._2cha.demo.review.service.ReviewService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Auth
@RestController
@RequiredArgsConstructor
public class ReviewController {

  private final ReviewService reviewService;

  @GetMapping("/places/{placeId}/reviews")
  public List<ReviewResponse> getPlaceReviews(@PathVariable Long placeId) {
    return reviewService.getReviewsByPlaceId(placeId);
  }

  @GetMapping("/members/{memberId}/reviews")
  public List<ReviewResponse> getMemberReviews(@PathVariable Long memberId) {
    return reviewService.getReviewsByMemberId(memberId);
  }

  @PostMapping("/places/{placeId}/reviews")
  public void writePlaceReview(@Authed Long memberId, @PathVariable Long placeId,
                               @Valid @RequestBody WriteReviewRequest dto) {
    reviewService.writeReview(memberId, placeId, dto.getTagIds(), dto.getImgUrls());
  }

  @DeleteMapping("/reviews/{reviewId}")
  public void deleteReview(@Authed Long memberId, @PathVariable Long reviewId) {
    reviewService.deleteReview(memberId, reviewId);
  }
}