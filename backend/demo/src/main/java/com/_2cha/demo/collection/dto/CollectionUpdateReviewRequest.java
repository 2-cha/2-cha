package com._2cha.demo.collection.dto;

import java.util.List;
import lombok.Data;

@Data
public class CollectionUpdateReviewRequest {

  private List<Long> reviewIds;
}
