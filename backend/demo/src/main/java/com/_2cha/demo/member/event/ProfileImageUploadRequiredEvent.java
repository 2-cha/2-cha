package com._2cha.demo.member.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;


@Getter
public class ProfileImageUploadRequiredEvent extends ApplicationEvent {

  private final Long memberId;
  private final String sourceImageUrl;

  public ProfileImageUploadRequiredEvent(Object source, Long memberId, String sourceImageUrl) {
    super(source);
    this.memberId = memberId;
    this.sourceImageUrl = sourceImageUrl;
  }
}
