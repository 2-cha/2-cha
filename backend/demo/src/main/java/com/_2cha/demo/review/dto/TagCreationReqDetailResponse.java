package com._2cha.demo.review.dto;

import com._2cha.demo.review.domain.Category;
import com._2cha.demo.review.domain.TagCreationRequest;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TagCreationReqDetailResponse {

  private Long id;
  private String acceptedEmoji;
  private String acceptedMessage;
  private String requestedEmoji;
  private String requestedMessage;
  private Long requesterId;
  private String requesterName;
  private Category category;
  private LocalDateTime requestedAt;

  public TagCreationReqDetailResponse(TagCreationRequest tagReq) {
    this.id = tagReq.getId();
    this.acceptedEmoji = tagReq.getAcceptedEmoji();
    this.acceptedMessage = tagReq.getAcceptedMessage();
    this.requestedEmoji = tagReq.getRequestedEmoji();
    this.requestedMessage = tagReq.getRequestedMessage();
    if (tagReq.getRequester() != null) {
      this.requesterId = tagReq.getRequester().getId();
      this.requesterName = tagReq.getRequester().getName();
    }
    this.requestedAt = tagReq.getRequestedAt();
    this.category = tagReq.getCategory();
  }
}
