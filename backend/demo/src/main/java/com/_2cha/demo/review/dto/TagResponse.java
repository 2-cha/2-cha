package com._2cha.demo.review.dto;

import com._2cha.demo.review.domain.Tag;
import lombok.Data;

@Data
public class TagResponse {

  private String emoji;
  private String message;

  public TagResponse(Tag tag) {
    this.emoji = tag.getEmoji();
    this.message = tag.getMsg();
  }
}
