package com._2cha.demo.collection.dto;

import com._2cha.demo.collection.domain.Collection;
import java.util.List;
import lombok.Data;

@Data
public class CollectionReviewsUpdatedResponse {

  private Long id;
  private String title;
  private List<Long> reviewIds;

  public CollectionReviewsUpdatedResponse(Collection collection) {
    this.id = collection.getId();
    this.title = collection.getTitle();
    this.reviewIds = collection.getReviews().stream().map(cr -> cr.getReview().getId()).toList();
  }
}
