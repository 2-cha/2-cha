package com._2cha.demo.recommendation.event;

import com._2cha.demo.recommendation.domain.Interaction;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CollectionInteractionCancelEvent extends ApplicationEvent {

  private final Long memberId;
  private final Long collId;
  private final Interaction interaction;

  public CollectionInteractionCancelEvent(Object source, Long memberId, Long collId,
                                          Interaction interaction) {
    super(source);
    this.memberId = memberId;
    this.collId = collId;
    this.interaction = interaction;
  }
}
