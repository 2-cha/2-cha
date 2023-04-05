package com._2cha.demo.review.dto;

import lombok.Data;

@Data
public class TagCountResponse {

  Long id;
  String emoji;
  String message;
  Integer count;
}
