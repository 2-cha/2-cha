package com._2cha.demo.review.repository;

import com._2cha.demo.review.domain.Review;
import java.util.List;

import com._2cha.demo.review.domain.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;


public interface ReviewRepository extends Repository<Review, Long> {

  Review save(Review review);

  Review findReviewById(Long id);

  List<Review> findReviewsByIdIn(List<Long> ids);

  List<Review> findReviewsByMemberId(Long memberId, Pageable pageParam);

  List<Review> findReviewsByPlaceId(Long placeId, Pageable pageParam);

  List<Review> findReviewsByPlaceId(Long placeId);

  List<Review> findReviewsByPlaceIdIn(List<Long> placeId);

  void deleteReviewById(Long id);

  List<Review> findAllByOrderByCreatedDesc();


  @Query
  List<Review> findByTagsIn(List<Tag> tags);
}
