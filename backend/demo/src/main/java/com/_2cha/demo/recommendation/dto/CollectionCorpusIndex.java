package com._2cha.demo.recommendation.dto;

import java.util.List;
import lombok.Data;

@Data
public class CollectionCorpusIndex implements Index {

  public static final int TOP_TAG_MESSAGE_SIZE = 3;

  public Long id;
  public String title;
  public String topTagMessagesCorpus;

  public CollectionCorpusIndex(Long id, String title, List<String> topTagMessages) {
    this.id = id;
    this.title = title;
    if (topTagMessages.size() > TOP_TAG_MESSAGE_SIZE) {
      topTagMessages = topTagMessages.subList(0, TOP_TAG_MESSAGE_SIZE);
    }
    this.topTagMessagesCorpus = String.join(" ", topTagMessages);
  }
}
