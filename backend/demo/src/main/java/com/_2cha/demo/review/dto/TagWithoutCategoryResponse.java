package com._2cha.demo.review.dto;

import com._2cha.demo.review.domain.Tag;
import lombok.Data;

@Data
public class TagWithoutCategoryResponse {

  private Long id;
  private String emoji;
  private String message;

  public TagWithoutCategoryResponse(Tag tag) {
    this.id = tag.getId();
    this.emoji = tag.getEmoji();
    this.message = tag.getMsg();
  }
}
