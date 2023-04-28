package com._2cha.demo.collection.repository;


import com._2cha.demo.collection.domain.ReviewInCollection;
import org.springframework.data.repository.Repository;

public interface ReviewInCollectionRepository extends Repository<ReviewInCollection, Long> {

  ReviewInCollection findByCollectionIdAndReviewId(Long collId, Long reviewId);
}
