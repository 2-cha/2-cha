package com._2cha.demo.review.repository;

import com._2cha.demo.review.domain.Review;
import java.util.List;
import org.springframework.data.repository.Repository;


public interface ReviewRepository extends Repository<Review, Long> {

  Review save(Review review);

  Review findReviewById(Long id);

  List<Review> findReviewsByMemberId(Long memberId);

  List<Review> findReviewsByPlaceId(Long placeId);

  List<Review> findReviewsByPlaceIdIn(List<Long> placeId);

  void deleteReviewById(Long id);
}
