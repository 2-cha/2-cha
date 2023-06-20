package com._2cha.demo.review.service;

import com._2cha.demo.member.domain.Member;
import com._2cha.demo.member.service.MemberService;
import com._2cha.demo.review.domain.Review;
import com._2cha.demo.review.domain.ReviewLike;
import com._2cha.demo.review.dto.LikeStatus;
import com._2cha.demo.review.exception.AlreadyLikedException;
import com._2cha.demo.review.exception.NoSuchReviewException;
import com._2cha.demo.review.exception.NotLikedException;
import com._2cha.demo.review.repository.ReviewLikeRepository;
import com._2cha.demo.review.repository.ReviewRepository;
import jakarta.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//TODO: 도메인 분리
@Service
@Transactional
@RequiredArgsConstructor
public class ReviewLikeService {

  private final ReviewLikeRepository likeRepository;
  private final ReviewRepository reviewRepository;
  private final MemberService memberService;

  /*-----------
   @ Commands
   ----------*/

  public void likeReview(Long memberId, Long reviewId) {
    ReviewLike like = likeRepository.findByMemberIdAndReviewId(memberId, reviewId);
    if (like != null) throw new AlreadyLikedException();

    Member member = memberService.findById(memberId);
    Review review = reviewRepository.findReviewById(reviewId);
    if (review == null) throw new NoSuchReviewException(reviewId);

    like = ReviewLike.createLike(member, review);

    likeRepository.save(like);
  }

  public void unlikeReview(Long memberId, Long reviewId) {
    ReviewLike like = likeRepository.findByMemberIdAndReviewId(memberId, reviewId);
    if (like == null) throw new NotLikedException();

    likeRepository.delete(like);
  }

  /*-----------
   @ Queries
   ----------*/
  public LikeStatus getLikeStatus(@Nullable Long memberId, Long reviewId) {
    return new LikeStatus(isLikedReview(memberId, reviewId), getCount(reviewId));
  }

  public Map<Long, LikeStatus> getLikeStatus(@Nullable Long memberId, List<Long> reviewIds) {
    Map<Long, LikeStatus> likeStatusMap = new HashMap<>();

    Map<Long, Long> countMap = getCount(reviewIds);
    Map<Long, Boolean> likedMap = areLikedReview(memberId, reviewIds);

    reviewIds.forEach(reviewId -> likeStatusMap.put(reviewId,
                                                    new LikeStatus(likedMap.get(reviewId),
                                                                   countMap.get(reviewId))));
    return likeStatusMap;
  }

  private Long getCount(Long reviewId) {
    // no check for review existence
    return likeRepository.countAllByReviewId(reviewId);
  }

  private Map<Long, Long> getCount(List<Long> reviewIds) {
    // no check for review existence
    return likeRepository.countAllGroupByReviewIdIn(reviewIds);
  }

  private boolean isLikedReview(@Nullable Long memberId, Long reviewId) {
    ReviewLike like = (memberId != null) ?
                      likeRepository.findByMemberIdAndReviewId(memberId, reviewId) :
                      null;

    return like != null;
  }

  private Map<Long, Boolean> areLikedReview(@Nullable Long memberId, List<Long> reviewIds) {
    Map<Long, Boolean> likedMap = new HashMap<>();
    reviewIds.forEach(reviewId -> likedMap.put(reviewId, false));

    List<ReviewLike> likes = (memberId != null) ?
                             likeRepository.findAllByMemberIdAndReviewIdIn(memberId, reviewIds) :
                             Collections.emptyList();

    likes.forEach(like -> likedMap.put(like.getReview().getId(), true));

    return likedMap;
  }
}
