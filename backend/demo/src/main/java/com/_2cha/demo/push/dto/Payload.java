package com._2cha.demo.push.dto;

import com._2cha.demo.push.validator.TopicName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@JsonInclude(Include.NON_EMPTY)
public class Payload {

  private String sub;
  @TopicName
  private String topic;
  private String condition;
  @NotEmpty
  private String title;
  @NotEmpty
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

  @AssertTrue(message = "Exactly one of sub, topic or condition must be specified")
  private boolean getSubOrTopicOrCondition() {
    int count = 0;
    count += sub != null ? 1 : 0;
    count += topic != null ? 1 : 0;
    count += condition != null ? 1 : 0;
    return count == 1;
  }

  public String getTarget() {
    if (sub != null) return sub;
    if (topic != null) return topic;
    if (condition != null) return condition;
    return null;
  }

  public static Payload withTopic(String topic, String title, String body, String data) {
    return new Payload(null, topic, null, title, body, data);
  }

  public static Payload withCondition(String condition, String title, String body, String data) {
    return new Payload(null, null, condition, title, body, data);
  }
}