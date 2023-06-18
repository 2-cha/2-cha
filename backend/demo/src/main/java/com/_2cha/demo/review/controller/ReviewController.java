package com._2cha.demo.review.controller;

import static com._2cha.demo.member.domain.Role.GUEST;
import static com._2cha.demo.member.domain.Role.MEMBER;
import static com._2cha.demo.util.GeomUtils.lat;
import static com._2cha.demo.util.GeomUtils.lon;
import static java.util.concurrent.CompletableFuture.completedFuture;

import com._2cha.demo.global.annotation.Auth;
import com._2cha.demo.global.annotation.Authed;
import com._2cha.demo.global.infra.imageupload.dto.ImageSavedResponse;
import com._2cha.demo.global.infra.imageupload.service.ImageUploadService;
import com._2cha.demo.global.validator.imagemime.ImageMime;
import com._2cha.demo.place.dto.PlaceSuggestionResponse;
import com._2cha.demo.place.service.PlaceService;
import com._2cha.demo.review.dto.ImageUrlWithSuggestionResponse;
import com._2cha.demo.review.dto.ReviewResponse;
import com._2cha.demo.review.dto.WriteReviewRequest;
import com._2cha.demo.review.service.LikeService;
import com._2cha.demo.review.service.ReviewService;
import com._2cha.demo.util.ImageUtils;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Validated
@RestController
@RequiredArgsConstructor
public class ReviewController {

  private final ReviewService reviewService;
  private final PlaceService placeService;
  private final LikeService likeService;
  private final ImageUploadService imageUploadService;

  @Auth(GUEST)
  @GetMapping("/places/{placeId}/reviews")
  public List<ReviewResponse> getPlaceReviews(@Authed Long memberId, @PathVariable Long placeId,
                                              Pageable pageParam) {
    List<ReviewResponse> reviews = reviewService.getReviewsByPlaceId(placeId, pageParam);
    reviewService.setResponseBookmarkStatus(memberId, reviews);
    reviewService.setResponseLikeStatus(memberId, reviews);
    return reviews;
  }

  @Auth(GUEST)
  @GetMapping("/members/{memberId}/reviews")
  public List<ReviewResponse> getMemberReviews(@Authed Long requesterId,
                                               @PathVariable Long memberId, Pageable pageParam) {
    List<ReviewResponse> reviews = reviewService.getReviewsByMemberId(memberId, pageParam);
    reviewService.setResponseBookmarkStatus(requesterId, reviews);
    reviewService.setResponseLikeStatus(requesterId, reviews);
    return reviews;
  }

  @Auth(GUEST)
  @GetMapping("/reviews/{reviewId}")
  public ReviewResponse getReview(@Authed Long memberId, @PathVariable Long reviewId) {
    ReviewResponse review = reviewService.getReviewById(reviewId);
    reviewService.setResponseBookmarkStatus(memberId, review);
    reviewService.setResponseLikeStatus(memberId, review);
    return review;
  }

  @Auth(MEMBER)
  @PostMapping("/places/{placeId}/reviews")
  public void writePlaceReview(@Authed Long memberId, @PathVariable Long placeId,
                               @Valid @RequestBody WriteReviewRequest dto) {
    reviewService.writeReview(memberId, placeId, dto.getTagIds(), dto.getImgUrls());
  }

  @Auth(MEMBER)
  @DeleteMapping("/reviews/{reviewId}")
  public void deleteReview(@Authed Long memberId, @PathVariable Long reviewId) {
    reviewService.deleteReview(memberId, reviewId);
  }

  @Auth(MEMBER)
  @PostMapping(value = "/reviews/images")
  public CompletableFuture<ImageUrlWithSuggestionResponse> saveReviewImage(
      @RequestParam @ImageMime MultipartFile file)
      throws IOException {
    Point point;
    byte[] imageBytes = file.getBytes();
    CompletableFuture<ImageSavedResponse> save = imageUploadService.save(imageBytes);
    CompletableFuture<List<PlaceSuggestionResponse>> suggestion = completedFuture(
        new ArrayList<>());
    if ((point = ImageUtils.getGeoPoint(imageBytes)) != null) {
      suggestion = placeService.suggestNearbyPlacesAsync(lat(point), lon(point));
    }

    return save.thenCombine(suggestion,
                            (res1, res2) -> new ImageUrlWithSuggestionResponse(res1.getUrl(), res2)
                           );
  }

  @Auth(MEMBER)
  @GetMapping("/bookmarks/reviews")
  public List<ReviewResponse> getBookmarkedPlaces(@Authed Long memberId) {
    return reviewService.getBookmarkedReviews(memberId);
  }

  @Auth(MEMBER)
  @PostMapping("/bookmarks/reviews/{reviewId}")
  public void createBookmark(@Authed Long memberId, @PathVariable Long reviewId) {
    reviewService.createBookmark(memberId, reviewId);
  }

  @Auth(MEMBER)
  @DeleteMapping("/bookmarks/reviews/{reviewId}")
  public void removeBookmark(@Authed Long memberId, @PathVariable Long reviewId) {
    reviewService.removeBookmark(memberId, reviewId);
  }

  @Auth(MEMBER)
  @PostMapping("/reviews/{reviewId}/like")
  public void like(@Authed Long memberId, @PathVariable Long reviewId) {
    likeService.likeReview(memberId, reviewId);
  }

  @Auth(MEMBER)
  @DeleteMapping("/reviews/{reviewId}/like")
  public void unlike(@Authed Long memberId, @PathVariable Long reviewId) {
    likeService.unlikeReview(memberId, reviewId);
  }
}