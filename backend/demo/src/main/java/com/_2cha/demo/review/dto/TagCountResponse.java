package com._2cha.demo.review.dto;

import com._2cha.demo.review.domain.Tag;
import lombok.Data;

@Data
public class TagCountResponse {

  private Long id;
  private String emoji;
  private String message;
  private Integer count;

  public TagCountResponse(Tag tag, Integer count) {
    this.id = tag.getId();
    this.emoji = tag.getEmoji();
    this.message = tag.getMsg();
    this.count = count;
  }
}
