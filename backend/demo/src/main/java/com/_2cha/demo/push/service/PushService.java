package com._2cha.demo.push.service;

import com._2cha.demo.global.event.PushEvent;
import com._2cha.demo.global.event.PushToMembersEvent;
import com._2cha.demo.push.dto.Payload;
import com._2cha.demo.push.dto.PayloadWithoutTarget;
import com._2cha.demo.push.dto.PushResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

//TODO: separate transaction and api call
// - manage tokens
public interface PushService {

  void register(Long memberId, String sub);

  void unregister(Long memberId, String sub);

  void unregisterAll(Long memberId);

  PushResponse subscribeToTopic(String sub, String topic);

  PushResponse subscribeToTopicForMember(Long memberId, String topic);

  PushResponse unsubscribeFromTopic(String sub, String topic);

  PushResponse unsubscribeFromTopicForMember(Long memberId, String topic);


  PushResponse send(Payload payload);

  PushResponse sendToMembers(List<Long> memberIds, PayloadWithoutTarget payload);

  CompletableFuture<PushResponse> subscribeToTopicAsync(String sub, String topic);

  CompletableFuture<PushResponse> subscribeToTopicForMemberAsync(Long memberId, String topic);

  CompletableFuture<PushResponse> unsubscribeFromTopicAsync(String sub, String topic);

  CompletableFuture<PushResponse> unsubscribeFromTopicForMemberAsync(Long memberId, String topic);

  CompletableFuture<PushResponse> handlePushEvent(PushEvent event);

  CompletableFuture<PushResponse> handlePushToMemberEvent(PushToMembersEvent event);

  CompletableFuture<PushResponse> sendAsync(Payload payload);

  CompletableFuture<PushResponse> sendToMembersAsync(List<Long> memberIds,
                                                     PayloadWithoutTarget payload);

  CompletableFuture<PushResponse> sendToMembersAsync(List<Long> memberIds,
                                                     String title, String body, Object data);
}
