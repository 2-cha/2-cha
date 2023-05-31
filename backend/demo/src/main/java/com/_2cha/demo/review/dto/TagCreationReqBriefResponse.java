package com._2cha.demo.review.dto;

import com._2cha.demo.review.domain.Category;
import com._2cha.demo.review.domain.TagCreationRequest;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TagCreationReqBriefResponse {

  private Long id;
  private String acceptedEmoji;
  private String acceptedMessage;
  private Category category;
  private LocalDateTime requestedAt;

  public TagCreationReqBriefResponse(TagCreationRequest tagReq) {
    this.id = tagReq.getId();
    this.acceptedEmoji = tagReq.getAcceptedEmoji();
    this.acceptedMessage = tagReq.getAcceptedMessage();
    this.requestedAt = tagReq.getRequestedAt();
    this.category = tagReq.getCategory();
  }
}
