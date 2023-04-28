package com._2cha.demo.collection.dto;

import lombok.Data;

@Data
public class CollectionUpdatedResponse {

  private Long id;
  private String title;
  private String description;
  private String thumbnail;
  private boolean exposure;
}
