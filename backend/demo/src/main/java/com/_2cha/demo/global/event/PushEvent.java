package com._2cha.demo.global.event;

import com._2cha.demo.push.dto.Payload;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PushEvent extends ApplicationEvent {

  private final Payload payload;

  protected PushEvent(Object source) {
    super(source);
    this.payload = null;
  }


  public PushEvent(Object source, String title, String body, String topic) {
    super(source);
    this.payload = Payload.withTopic(topic, title, body, null);
  }


  public PushEvent(Object source, String title, String body, Object data, String topic) {
    this(source, title, body, topic);
    this.payload.setData(data);
  }
}
