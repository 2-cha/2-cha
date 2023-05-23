package com._2cha.demo.push.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@JsonInclude(Include.NON_EMPTY)
public class Payload {

  private String sub;
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

  @AssertTrue(message = "Invalid topic name")
  private boolean isTopicValid() {
    return topic == null || topic.matches("[a-zA-Z0-9-_.~%]+");
  }
}