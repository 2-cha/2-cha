package com._2cha.demo.review.dto;

import com._2cha.demo.place.dto.PlaceBriefResponse;
import lombok.Data;

@Data
public class MemberReviewResponse extends ReviewResponse {

  PlaceBriefResponse place;
}
