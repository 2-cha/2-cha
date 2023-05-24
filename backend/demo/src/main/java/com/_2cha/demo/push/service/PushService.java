package com._2cha.demo.push.service;

import com._2cha.demo.push.dto.Payload;

public interface PushService {

  void register(Long memberId, String sub);

  void unregister(Long memberId, String sub);

  void unregisterAll(Long memberId);

  PushResponse subscribeToTopic(Long memberId, String topic);

  PushResponse unsubscribeFromTopic(Long memberId, String topic);

  PushResponse send(Payload payload);

  PushResponse sendToMembers(List<Long> memberIds, PayloadWithoutTarget payload);
}
