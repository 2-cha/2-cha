package com._2cha.demo.review.dto;

import com._2cha.demo.review.domain.TagCreationRequest;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class MakeTagReqResponse {

  private Long id;
  private String requestedEmoji;
  private String requestedMessage;
  private LocalDateTime requestedAt;


  public MakeTagReqResponse(TagCreationRequest tagReq) {
    this.id = tagReq.getId();
    this.requestedEmoji = tagReq.getRequestedEmoji();
    this.requestedMessage = tagReq.getRequestedMessage();
    this.requestedAt = tagReq.getRequestedAt();
  }
}
