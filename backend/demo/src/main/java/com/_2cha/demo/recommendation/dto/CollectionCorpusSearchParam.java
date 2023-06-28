package com._2cha.demo.recommendation.dto;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class CollectionCorpusSearchParam {

  public static final int TOP_TAG_MESSAGE_SIZE = 3;

  public Long id;
  public String title;
  public String topTagMessagesCorpus;

  public CollectionCorpusSearchParam(Long id, String title, List<String> topTagMessages) {
    this.id = id;
    this.title = title;
    if (topTagMessages.size() > TOP_TAG_MESSAGE_SIZE) {
      topTagMessages = topTagMessages.subList(0, TOP_TAG_MESSAGE_SIZE);
    }
    this.topTagMessagesCorpus = String.join(" ", topTagMessages);
  }

  public Map<String, String> getFields() {
    Map<String, String> m = new HashMap<>();
    Field[] fields = this.getClass().getFields();
    // exclude static fields
    List<String> names = Arrays.stream(fields)
                               .filter(f -> !Modifier.isStatic(f.getModifiers()))
                               .map(Field::getName)
                               .toList();

    for (String name : names) {
      try {
        Object o = this.getClass().getField(name).get(this);
        m.put(name, o != null ? o.toString() : "");
      } catch (Exception e) {
        log.warn("Skip mapping field [{}] due to error: {}", name, e.getMessage());
      }
    }

    return Map.copyOf(m);
  }
}
