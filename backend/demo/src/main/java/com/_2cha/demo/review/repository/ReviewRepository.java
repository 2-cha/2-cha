package com._2cha.demo.review.repository;

import com._2cha.demo.review.domain.Review;
import com._2cha.demo.review.domain.Tag;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;


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

  List<Review> findReviewsByTagsInReviewTagIn(List<Tag> tags);

  @Query("SELECT DISTINCT r FROM Review r JOIN r.tagsInReview tir WHERE r.place.id IN :placeIds AND tir.tag.id IN :tagIds")
  List<Review> findReviewByPlaceIdInAndTagsInReviewTagIdIn(@Param("placeIds") List<Long> placeIds, @Param("tagIds") List<Long> tagIds);
}
