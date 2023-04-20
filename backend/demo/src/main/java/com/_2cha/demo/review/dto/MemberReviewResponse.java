package com._2cha.demo.review.dto;

import com._2cha.demo.place.dto.PlaceBriefResponse;
import com._2cha.demo.review.domain.Review;
import lombok.Data;

@Data
public class MemberReviewResponse extends ReviewResponse {

  public MemberReviewResponse(Review review) {
    super(review);
  }

  private PlaceBriefResponse place;
}
