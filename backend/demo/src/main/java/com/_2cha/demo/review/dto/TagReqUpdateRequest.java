package com._2cha.demo.review.dto;

import com._2cha.demo.review.domain.Category;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TagReqUpdateRequest {

  @NotEmpty
  private String emoji;
  @NotEmpty
  private String message;
  @NotNull
  private Category category;
}
