package com._2cha.demo.collection.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CollectionViewResponse {

  private Long id;
  private String title;
  private String description;
  private String thumbnail;
}
