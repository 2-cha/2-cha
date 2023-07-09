package com._2cha.demo.global.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MemberDeletedEvent extends ApplicationEvent {

  private final Long memberId;

  public MemberDeletedEvent(Object source, Long memberId) {
    super(source);
    this.memberId = memberId;
  }
}
