package com._2cha.demo.review.dto;

import lombok.Data;

@Data
public class TagWithIdResponse {

  Long id;
  String emoji;
  String message;
}
