package com._2cha.demo.collection.controller;

import com._2cha.demo.collection.dto.CollectionCreateRequest;
import com._2cha.demo.collection.dto.CollectionCreatedResponse;
import com._2cha.demo.collection.dto.CollectionRemovedResponse;
import com._2cha.demo.collection.dto.CollectionReviewsResponse;
import com._2cha.demo.collection.dto.CollectionReviewsUpdateRequest;
import com._2cha.demo.collection.dto.CollectionReviewsUpdatedResponse;
import com._2cha.demo.collection.dto.CollectionUpdateRequest;
import com._2cha.demo.collection.dto.CollectionUpdatedResponse;
import com._2cha.demo.collection.dto.CollectionViewResponse;
import com._2cha.demo.collection.service.CollectionService;
import com._2cha.demo.global.annotation.Auth;
import com._2cha.demo.global.annotation.Authed;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CollectionController {

  private final CollectionService collectionService;

  @Auth
  @GetMapping("/members/{memberId}/collections")
  public List<CollectionViewResponse> getMemberCollections(@Authed Long requesterId,
                                                           @PathVariable Long memberId) {
    List<CollectionViewResponse> collections = collectionService.getMemberCollections(
        memberId, true);
    collectionService.setResponseBookmarkStatus(requesterId, collections);
    return collections;
  }

  @Auth
  @GetMapping("/collections")
  public List<CollectionViewResponse> getCollectionsIncludingPrivate(@Authed Long memberId) {
    List<CollectionViewResponse> collections = collectionService.getMemberCollections(
        memberId, false);
    collectionService.setResponseBookmarkStatus(memberId, collections);
    return collections;
  }

  @Auth
  @PostMapping("/collections")
  public CollectionCreatedResponse createCollection(@Authed Long memberId,
                                                    @RequestBody @Valid CollectionCreateRequest dto) {
    return collectionService.createCollection(memberId, dto.getTitle(), dto.getDescription(),
                                              dto.getThumbnail(), dto.getReviewIds());
  }

  @Auth
  @GetMapping("/collections/{collId}")
  public CollectionReviewsResponse getCollectionDetail(@Authed Long memberId,
                                                       @PathVariable Long collId) {
    return collectionService.getCollectionDetail(memberId, collId);
  }

  @Auth
  @DeleteMapping("/collections/{collId}")
  public CollectionRemovedResponse removeCollection(@Authed Long memberId,
                                                    @PathVariable Long collId) {
    return collectionService.removeCollection(memberId, collId);
  }

  /**
   * exposure / title / description / thumbnail
   */
  @Auth
  @PatchMapping("/collections/{collId}")
  public CollectionUpdatedResponse updateCollection(@Authed Long memberId,
                                                    @PathVariable Long collId,
                                                    @RequestBody @Valid CollectionUpdateRequest dto) {
    return collectionService.updateCollection(memberId, collId,
                                              dto.getTitle(), dto.getDescription(),
                                              dto.getThumbnail(), dto.getExposure());
  }

  @Auth
  @PutMapping("/collections/{collId}/reviews")
  public CollectionReviewsUpdatedResponse updateReviews(@Authed Long memberId,
                                                        @PathVariable Long collId,
                                                        @RequestBody @Valid CollectionReviewsUpdateRequest dto) {
    return collectionService.updateReviews(memberId, collId, dto.getReviewIds());
  }

  @Auth
  @GetMapping("/bookmarks/collections")
  public List<CollectionViewResponse> getBookmarkedPlaces(@Authed Long memberId) {
    return collectionService.getBookmarkedCollections(memberId);
  }

  @Auth
  @PostMapping("/bookmarks/collections/{collId}")
  public void createBookmark(@Authed Long memberId, @PathVariable Long collId) {
    collectionService.createBookmark(memberId, collId);
  }

  @Auth
  @DeleteMapping("/bookmarks/collections/{collId}")
  public void removeBookmark(@Authed Long memberId, @PathVariable Long collId) {
    collectionService.removeBookmark(memberId, collId);
  }
}
