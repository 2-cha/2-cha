package com._2cha.demo.review.dto;

import com._2cha.demo.review.domain.Category;
import com._2cha.demo.review.domain.Tag;
import com._2cha.demo.review.domain.TagCreationRequest;
import lombok.Data;

@Data
public class TagReqAcceptedResponse {

  private Long requestId;
  private Long tagId;
  private String message;
  private String emoji;
  private Category category;

  public TagReqAcceptedResponse(TagCreationRequest req, Tag tag) {
    this.requestId = req.getId();
    this.tagId = tag.getId();
    this.message = tag.getMsg();
    this.emoji = tag.getEmoji();
    this.category = tag.getCategory();
  }
}
