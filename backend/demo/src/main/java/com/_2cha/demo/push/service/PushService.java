package com._2cha.demo.push.service;

import com._2cha.demo.push.dto.Payload;

public interface PushService {

  void register(Long memberId, String sub);

  void unregister(Long memberId, String sub);

  void unregisterAll(Long memberId);

  void subscribeToTopic(Long memberId, String topic);

  void unsubscribeFromTopic(Long memberId, String topic);

  void send(Payload payload);
}
