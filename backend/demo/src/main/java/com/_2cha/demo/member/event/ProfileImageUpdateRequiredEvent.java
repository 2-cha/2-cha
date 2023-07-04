package com._2cha.demo.member.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;


@Getter
public class ProfileImageUpdateRequiredEvent extends ApplicationEvent {

  private final Long memberId;
  private final String thumbUrlPath;

  public ProfileImageUpdateRequiredEvent(Object source, Long memberId, String thumbUrlPath) {
    super(source);
    this.memberId = memberId;
    this.thumbUrlPath = thumbUrlPath;
  }
}
