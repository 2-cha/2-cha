package com._2cha.demo.review.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MakeTagReqRequest {

  @NotEmpty
  private String message;
  @NotEmpty
  private String emoji;
}
