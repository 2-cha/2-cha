package com._2cha.demo.review.dto;

import com._2cha.demo.member.dto.MemberProfileResponse;
import com._2cha.demo.review.domain.Review;
import lombok.Data;

@Data
public class PlaceReviewResponse extends ReviewResponse {

  public PlaceReviewResponse(Review review) {
    super(review);
  }

  private MemberProfileResponse member;
}
