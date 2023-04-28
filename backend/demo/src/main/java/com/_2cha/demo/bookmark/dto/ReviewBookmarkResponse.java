package com._2cha.demo.bookmark.dto;

import com._2cha.demo.review.dto.ReviewResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewBookmarkResponse {

  private Long id;
  private ReviewResponse review;
}
