package com._2cha.demo.global.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class FirstReviewCreatedEvent extends ApplicationEvent {

  private final Long placeId;
  private final String imageUrlPath;
  private final String thumbUrlPath;

  public FirstReviewCreatedEvent(Object source, Long placeId, String imageUrlPath,
                                 String thumbUrlPath) {
    super(source);
    this.placeId = placeId;
    this.imageUrlPath = imageUrlPath;
    this.thumbUrlPath = thumbUrlPath;
  }
}
