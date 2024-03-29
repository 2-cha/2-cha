package com._2cha.demo.collection.controller;

import static com._2cha.demo.member.domain.Role.ADMIN;
import static com._2cha.demo.member.domain.Role.GUEST;
import static com._2cha.demo.member.domain.Role.MEMBER;

import com._2cha.demo.collection.dto.CollectionBriefResponse;
import com._2cha.demo.collection.dto.CollectionCreateRequest;
import com._2cha.demo.collection.dto.CollectionCreatedResponse;
import com._2cha.demo.collection.dto.CollectionDetailResponse;
import com._2cha.demo.collection.dto.CollectionReviewsUpdateRequest;
import com._2cha.demo.collection.dto.CollectionReviewsUpdatedResponse;
import com._2cha.demo.collection.dto.CollectionUpdateRequest;
import com._2cha.demo.collection.dto.CollectionUpdatedResponse;
import com._2cha.demo.collection.service.CollectionLikeService;
import com._2cha.demo.collection.service.CollectionService;
import com._2cha.demo.global.annotation.Auth;
import com._2cha.demo.global.annotation.Authed;
import com._2cha.demo.recommendation.service.CollectionRecommendationService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CollectionController {

  private final CollectionService collectionService;
  private final CollectionLikeService likeService;
  private final CollectionRecommendationService recommendationService;

  @Auth(GUEST)
  @GetMapping("/members/{memberId}/collections")
  public List<CollectionBriefResponse> getMemberCollections(@Authed Long requesterId,
                                                            @PathVariable Long memberId) {
    return collectionService.getMemberCollections(requesterId, memberId);
  }

  @Auth(GUEST)
  @GetMapping("/collections")
  public List<CollectionBriefResponse> getCollections(@Authed Long memberId,
                                                      @RequestParam Double lat,
                                                      @RequestParam Double lon,
                                                      @RequestParam(defaultValue = "5000.0") Double distance
                                                     ) {
    return collectionService.getRecommendations(memberId, lat, lon, distance);
  }

  @Auth(GUEST)
  @GetMapping("/collections/{collId}/recommendation")
  public List<CollectionBriefResponse> recommend(@Authed Long memberId,
                                                 @PathVariable Long collId,
                                                 @RequestParam(name = "max_size", defaultValue = "10") Integer maxSize) {
    return collectionService.getSimilarCollections(memberId, collId, maxSize);
  }

  @Auth(MEMBER)
  @PostMapping("/collections")
  public CollectionCreatedResponse createCollection(@Authed Long memberId,
                                                    @RequestBody @Valid CollectionCreateRequest dto) {
    return collectionService.createCollection(memberId, dto.getTitle(), dto.getDescription(),
                                              dto.getThumbnail(), dto.getReviewIds());
  }

  @Auth(MEMBER)
  @GetMapping("/collections/nearby")
  public List<CollectionBriefResponse> nbCollection(
      @RequestParam(name = "max_dist", required = false, defaultValue = "1000.0") Double maxDist,
      @RequestParam Double lat,
      @RequestParam Double lon) {
    return collectionService.getNearbyCollections(lat, lon, maxDist);
  }

  @Auth(GUEST)
  @GetMapping("/collections/{collId}")
  public CollectionDetailResponse getCollectionDetail(@Authed Long memberId,
                                                      @PathVariable Long collId) {
    return collectionService.getCollectionDetail(memberId, collId);
  }

  @Auth(MEMBER)
  @DeleteMapping("/collections/{collId}")
  public void removeCollection(@Authed Long memberId, @PathVariable Long collId) {
    collectionService.removeCollection(memberId, collId);
  }

  /**
   * exposure / title / description / thumbnail
   */
  @Auth(MEMBER)
  @PatchMapping("/collections/{collId}")
  public CollectionUpdatedResponse updateCollection(@Authed Long memberId,
                                                    @PathVariable Long collId,
                                                    @RequestBody @Valid CollectionUpdateRequest dto) {
    return collectionService.updateCollection(memberId, collId,
                                              dto.getTitle(), dto.getDescription(),
                                              dto.getThumbnail(), dto.getExposure());
  }

  @Auth(MEMBER)
  @PutMapping("/collections/{collId}/reviews")
  public CollectionReviewsUpdatedResponse updateReviews(@Authed Long memberId,
                                                        @PathVariable Long collId,
                                                        @RequestBody @Valid CollectionReviewsUpdateRequest dto) {
    return collectionService.updateReviews(memberId, collId, dto.getReviewIds());
  }

  @Auth(MEMBER)
  @GetMapping("/bookmarks/collections")
  public List<CollectionBriefResponse> getBookmarkedPlaces(@Authed Long memberId) {
    return collectionService.getBookmarkedCollections(memberId);
  }

  @Auth(MEMBER)
  @PostMapping("/bookmarks/collections/{collId}")
  public void createBookmark(@Authed Long memberId, @PathVariable Long collId) {
    collectionService.createBookmark(memberId, collId);
  }

  @Auth(MEMBER)
  @DeleteMapping("/bookmarks/collections/{collId}")
  public void removeBookmark(@Authed Long memberId, @PathVariable Long collId) {
    collectionService.removeBookmark(memberId, collId);
  }

  @Auth(MEMBER)
  @PostMapping("/collections/{collId}/like")
  public void like(@Authed Long memberId, @PathVariable Long collId) {
    likeService.likeCollection(memberId, collId);
  }

  @Auth(MEMBER)
  @DeleteMapping("/collections/{collId}/like")
  public void unlike(@Authed Long memberId, @PathVariable Long collId) {
    likeService.unlikeCollection(memberId, collId);
  }

  //TODO: cron job
  @Auth(ADMIN)
  @GetMapping("/collections/mass")
  public void massIndex() throws InterruptedException {
    recommendationService.massIndex();
  }
}
