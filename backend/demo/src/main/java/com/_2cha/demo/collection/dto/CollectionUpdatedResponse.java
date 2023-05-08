package com._2cha.demo.collection.dto;

import com._2cha.demo.collection.domain.Collection;
import lombok.Data;

@Data
public class CollectionUpdatedResponse {

  private Long id;
  private String title;
  private String description;
  private String thumbnail;
  private boolean exposure;

  public CollectionUpdatedResponse(Collection collection, String baseUrl) {
    this.id = collection.getId();
    this.title = collection.getTitle();
    this.description = collection.getDescription();
    this.thumbnail = baseUrl + collection.getThumbnailUrlPath();
    this.exposure = collection.isExposed();
  }
}
