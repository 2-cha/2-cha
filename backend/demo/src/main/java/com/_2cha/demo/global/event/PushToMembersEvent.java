package com._2cha.demo.global.event;

import com._2cha.demo.push.dto.PayloadWithoutTarget;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PushToMembersEvent extends ApplicationEvent {

  private final PayloadWithoutTarget payload = new PayloadWithoutTarget();
  private final List<Long> receivers = new ArrayList<>();

  protected PushToMembersEvent(Object source) {
    super(source);
  }


  public PushToMembersEvent(Object source, String title, String body, List<Long> receivers) {
    super(source);
    this.payload.setTitle(title);
    this.payload.setBody(body);
    this.receivers.addAll(receivers);
  }

  public PushToMembersEvent(Object source, String title, String body, Long... receivers) {
    super(source);
    this.payload.setTitle(title);
    this.payload.setBody(body);
    Collections.addAll(this.receivers, receivers);
  }

  public PushToMembersEvent(Object source, String title, String body, Object data,
                            List<Long> receivers) {
    this(source, title, body, receivers);
    this.payload.setData(data);
  }

  public PushToMembersEvent(Object source, String title, String body, Object data,
                            Long... receivers) {
    this(source, title, body, receivers);
    this.payload.setData(data);
  }
}
