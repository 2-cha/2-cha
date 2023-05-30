package com._2cha.demo.review.repository;

import com._2cha.demo.review.domain.ReviewBookmark;
import java.util.List;
import org.springframework.data.repository.Repository;


public interface ReviewBookmarkRepository extends Repository<ReviewBookmark, Long> {

  ReviewBookmark save(ReviewBookmark bookmark);

  ReviewBookmark findByMemberIdAndReviewId(Long memberId, Long reviewId);

  List<ReviewBookmark> findAllByMemberIdAndReviewIdIn(Long memberId, List<Long> reviewIds);

  List<ReviewBookmark> findAllByMemberId(Long memberId);

  void delete(ReviewBookmark bookmark);
}
