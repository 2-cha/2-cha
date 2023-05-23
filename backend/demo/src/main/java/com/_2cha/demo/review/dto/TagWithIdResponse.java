package com._2cha.demo.review.dto;

import com._2cha.demo.review.domain.Category;
import com._2cha.demo.review.domain.Tag;
import lombok.Data;

@Data
public class TagWithIdResponse {

  private Long id;
  private String emoji;
  private String message;
  private Category category;

  public TagWithIdResponse(Tag tag) {
    this.id = tag.getId();
    this.emoji = tag.getEmoji();
    this.message = tag.getMsg();
    this.category = tag.getCategory();
  }
}
