package com._2cha.demo.review.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;


@Data
public class WriteReviewRequest {

  @NotNull
  List<Long> tagIds;
  @NotNull
  List<String> imgUrls;
}
