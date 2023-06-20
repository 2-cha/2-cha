package com._2cha.demo.review.repository;

import com._2cha.demo.review.domain.ReviewLike;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

  ReviewLike findByMemberIdAndReviewId(Long memberId, Long reviewId);

  List<ReviewLike> findAllByMemberIdAndReviewIdIn(Long memberId, List<Long> reviewIds);

  Long countAllByReviewId(Long reviewId);

  Map<Long, Long> countAllGroupByReviewIdIn(List<Long> reviewIds);
}
