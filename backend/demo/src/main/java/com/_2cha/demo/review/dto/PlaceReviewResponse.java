package com._2cha.demo.review.dto;

import com._2cha.demo.member.dto.MemberProfileResponse;
import lombok.Data;

@Data
public class PlaceReviewResponse extends ReviewResponse {

  MemberProfileResponse member;
}
