package com._2cha.demo.collection.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Data;

@Data
public class CollectionReviewsUpdateRequest {

  @NotEmpty
  private List<Long> reviewIds;
}
