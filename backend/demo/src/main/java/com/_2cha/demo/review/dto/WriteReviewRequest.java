package com._2cha.demo.review.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;


@Data
public class WriteReviewRequest {

  @NotNull
  @Size(min = 1, max = 5)
  List<Long> tagIds;
  @NotNull
  List<String> imgUrls;
}
