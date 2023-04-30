package com._2cha.demo.collection.dto;

import com._2cha.demo.collection.domain.Collection;
import lombok.Data;

@Data
public class CollectionCreatedResponse {

  private Long id;
  private String title;

  public CollectionCreatedResponse(Collection collection) {
    this.id = collection.getId();
    this.title = collection.getTitle();
  }
}
