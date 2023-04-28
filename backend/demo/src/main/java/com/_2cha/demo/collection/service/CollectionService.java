package com._2cha.demo.collection.service;

import com._2cha.demo.collection.domain.Collection;
import com._2cha.demo.collection.dto.CollectionCreatedResponse;
import com._2cha.demo.collection.dto.CollectionRemovedResponse;
import com._2cha.demo.collection.dto.CollectionReviewsResponse;
import com._2cha.demo.collection.dto.CollectionUpdatedResponse;
import com._2cha.demo.collection.dto.CollectionViewResponse;
import com._2cha.demo.collection.repository.CollectionQueryRepository;
import com._2cha.demo.collection.repository.CollectionRepository;
import com._2cha.demo.collection.repository.ReviewInCollectionRepository;
import com._2cha.demo.global.exception.ForbiddenException;
import com._2cha.demo.global.exception.NotFoundException;
import com._2cha.demo.member.domain.Member;
import com._2cha.demo.member.service.MemberService;
import com._2cha.demo.review.domain.Review;
import com._2cha.demo.review.service.ReviewService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CollectionService {

  private final CollectionRepository collectionRepository;
  private final ReviewInCollectionRepository revInCollRepository;
  private final CollectionQueryRepository collectionQueryRepository;
  private final MemberService memberService;
  private final ReviewService reviewService;

  /*-----------
   @ Queries
   ----------*/
  @Transactional(readOnly = true)
  public List<CollectionViewResponse> getMemberCollections(Long memberId, boolean exposedOnly) {
    List<CollectionViewResponse> memberCollections = collectionQueryRepository.getMemberCollections(
        memberId, exposedOnly);
    return memberCollections;
  }

  //TODO
  @Transactional(readOnly = true)
  public CollectionReviewsResponse getCollectionDetail(Long memberId, Long collId) {
    // member check
    return collectionQueryRepository.getCollectionDetail(collId);
  }


  /*-----------
   @ Commands
   ----------*/
  public CollectionCreatedResponse createCollection(Long memberId, String title,
                                                    String description, String thumbnail,
                                                    List<Long> reviewIds) {
    Member member = memberService.findById(memberId);
    List<Review> reviews = reviewService.findReviewsByIdIn(reviewIds);
    for (Review review : reviews) {
      if (!review.getMember().getId().equals(memberId)) {
        throw new ForbiddenException("Cannot create collection with other member's review.");
      }
    }
    Collection collection = Collection.createCollection(member, title, description, thumbnail,
                                                        reviews);

    collectionRepository.save(collection);

    return new CollectionCreatedResponse(collection);
  }

  public CollectionRemovedResponse removeCollection(Long memberId, Long collId) {
    Collection collection = collectionRepository.findCollectionById(collId);
    if (collection == null) {
      throw new NotFoundException("No collection with such id.", "noSuchCollection");
    }

    if (!collection.getMember().getId().equals(memberId)) {
      throw new ForbiddenException("Cannot remove other member's collection.");
    }

    collectionRepository.deleteCollectionById(collection.getId());
    return new CollectionRemovedResponse(); //TODO: DTO
  }

  public CollectionUpdatedResponse updateCollection(Long memberId, Long collId, String title,
                                                    String description, String thumbnail,
                                                    Boolean exposure) {
    Collection collection = collectionRepository.findCollectionById(collId);
    if (collection == null) {
      throw new NotFoundException("No collection with such id.", "noSuchCollection");
    }

    if (!collection.getMember().getId().equals(memberId)) {
      throw new ForbiddenException("Cannot update other member's collection.");
    }

    // for PATCH method
    if (title != null) collection.updateTitle(title);
    if (description != null) collection.updateDescription(description);
    if (thumbnail != null) collection.updateThumbnail(thumbnail);
    if (exposure != null) collection.toggleExposure(exposure);

    return new CollectionUpdatedResponse(); //TODO: DTO
  }

  public CollectionUpdatedResponse updateReviews(Long memberId, Long collId, List<Long> reviewIds) {
    Collection collection = collectionRepository.findCollectionById(collId);
    if (collection == null) {
      throw new NotFoundException("No collection with such id.", "noSuchCollection");
    }

    if (!collection.getMember().getId().equals(memberId)) {
      throw new ForbiddenException("Cannot update other member's collection.");
    }

    List<Review> reviews = reviewService.findReviewsByIdIn(reviewIds);
    for (Review review : reviews) {
      if (!review.getMember().getId().equals(memberId)) {
        throw new ForbiddenException("Cannot create collection with other member's review.");
      }
    }

    collection.updateReviews(reviews);

    return new CollectionUpdatedResponse();
  }
}
