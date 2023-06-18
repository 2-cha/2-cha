package com._2cha.demo.review.repository;

import com._2cha.demo.review.domain.Like;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

  Like findByMemberIdAndReviewId(Long memberId, Long reviewId);

  List<Like> findAllByMemberIdAndReviewIdIn(Long memberId, List<Long> reviewIds);

  Long countAllByReviewId(Long reviewId);

  Map<Long, Long> countAllGroupByReviewIdIn(List<Long> reviewIds);
}
