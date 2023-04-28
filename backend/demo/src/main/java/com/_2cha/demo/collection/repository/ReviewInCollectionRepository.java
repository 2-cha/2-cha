package com._2cha.demo.collection.repository;


import com._2cha.demo.collection.domain.ReviewInCollection;
import com._2cha.demo.collection.domain.ReviewInCollectionId;
import org.springframework.data.repository.Repository;

public interface ReviewInCollectionRepository extends
                                              Repository<ReviewInCollection, ReviewInCollectionId> {

  ReviewInCollection findByCollectionIdAndReviewId(Long collId, Long reviewId);
}
