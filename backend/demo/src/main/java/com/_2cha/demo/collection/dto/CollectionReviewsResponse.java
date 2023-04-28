package com._2cha.demo.collection.dto;

import com._2cha.demo.collection.domain.Collection;
import com._2cha.demo.review.dto.MemberReviewResponse;
import java.util.List;
import lombok.Data;

@Data
public class CollectionReviewsResponse {

  private Long id;
  private String title;
  private String description;
  private String thumbnail;
  private List<MemberReviewResponse> reviews;

  public CollectionReviewsResponse(Collection collection, List<MemberReviewResponse> reviews) {
    this.id = collection.getId();
    this.title = collection.getTitle();
    this.description = collection.getDescription();
    this.thumbnail = collection.getThumbnail();
    this.reviews = reviews;
  }
}

