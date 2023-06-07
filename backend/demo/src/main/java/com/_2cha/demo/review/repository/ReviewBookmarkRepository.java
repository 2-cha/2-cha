package com._2cha.demo.review.repository;

import com._2cha.demo.bookmark.dto.BookmarkCountProjection;
import com._2cha.demo.review.domain.ReviewBookmark;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;


public interface ReviewBookmarkRepository extends Repository<ReviewBookmark, Long> {

  ReviewBookmark save(ReviewBookmark bookmark);

  ReviewBookmark findByMemberIdAndReviewId(Long memberId, Long reviewId);

  List<ReviewBookmark> findAllByMemberIdAndReviewIdIn(Long memberId, List<Long> reviewIds);

  List<ReviewBookmark> findAllByMemberId(Long memberId);

  @Query("SELECT b.review.id as id, count(b) as count " +
         "FROM ReviewBookmark b " +
         "WHERE b.review.id in ?1 GROUP BY id")
  List<BookmarkCountProjection> countAllByReviewIdIn(List<Long> reviewId);

  Long countAllByReviewId(Long reviewId);

  void delete(ReviewBookmark bookmark);
}
