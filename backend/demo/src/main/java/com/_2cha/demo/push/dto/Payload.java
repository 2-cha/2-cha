package com._2cha.demo.push.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@Data
@JsonInclude(Include.NON_EMPTY)
public class Payload {

  private String sub;
  private String topic;
  private String condition;
  private String title;
  private String body;
  private Object data;

  public Payload(String sub, String topic, String condition, String title, String body,
                 Object data) {
    this.sub = sub;
    this.topic = topic;
    this.condition = condition;
    this.title = title;
    this.body = body;
    this.data = data;
  }
}