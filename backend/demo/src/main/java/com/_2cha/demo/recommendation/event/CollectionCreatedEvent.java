package com._2cha.demo.recommendation.event;

import com._2cha.demo.recommendation.dto.CollectionCorpusDocumentSource;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CollectionCreatedEvent extends ApplicationEvent {

  private final CollectionCorpusDocumentSource docSource;

  public CollectionCreatedEvent(Object source,
                                CollectionCorpusDocumentSource docSource) {
    super(source);
    this.docSource = docSource;
  }
}
