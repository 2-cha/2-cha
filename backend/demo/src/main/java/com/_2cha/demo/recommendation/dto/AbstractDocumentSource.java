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
public class AbstractDocumentSource implements DocumentSource {

  public final Long id;

  public AbstractDocumentSource(Long id) {
    this.id = id;
  }

  @Override
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
