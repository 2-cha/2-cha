package com._2cha.demo.recommendation.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class CollectionCorpusDocumentSource extends AbstractDocumentSource {

  public static final int TOP_TAG_MESSAGE_SIZE = 3;

  public String title;
  public String topTagMessagesCorpus;

  public CollectionCorpusDocumentSource(Long id, String title, List<String> topTagMessages) {
    super(id);
    this.title = title;
    if (topTagMessages.size() > TOP_TAG_MESSAGE_SIZE) {
      topTagMessages = topTagMessages.subList(0, TOP_TAG_MESSAGE_SIZE);
    }
    this.topTagMessagesCorpus = String.join(" ", topTagMessages);
  }
}
