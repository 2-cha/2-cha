package com._2cha.demo.review.controller;

import com._2cha.demo.global.annotation.Auth;
import com._2cha.demo.global.annotation.Authed;
import com._2cha.demo.global.infra.imageupload.dto.ImageSavedResponse;
import com._2cha.demo.global.infra.imageupload.service.ImageUploadService;
import com._2cha.demo.global.validator.imagemime.ImageMime;
import com._2cha.demo.review.dto.ReviewResponse;
import com._2cha.demo.review.dto.WriteReviewRequest;
import com._2cha.demo.review.service.ReviewService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Auth
@Validated
@RestController
@RequiredArgsConstructor
public class ReviewController {

  private final ReviewService reviewService;
  private final ImageUploadService imageUploadService;

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

  @PostMapping(value = "/reviews/images")
  public ImageSavedResponse saveReviewImage(@RequestParam @ImageMime MultipartFile file)
      throws IOException {
    return imageUploadService.save(file.getBytes());
  }
}