package com._2cha.demo.review.dto;

import com._2cha.demo.review.domain.Category;
import com._2cha.demo.review.domain.TagCreationRequest;
import lombok.Data;

@Data
public class TagReqUpdatedResponse {

  private Long id;
  private String emoji;
  private String message;
  private Category category;

  public TagReqUpdatedResponse(TagCreationRequest tagReq) {
    this.id = tagReq.getId();
    this.emoji = tagReq.getAcceptedEmoji();
    this.message = tagReq.getAcceptedMessage();
    this.category = tagReq.getCategory();
  }
}
