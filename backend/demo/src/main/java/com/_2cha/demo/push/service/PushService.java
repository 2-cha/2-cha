package com._2cha.demo.push.service;

import com._2cha.demo.global.event.PushEvent;
import com._2cha.demo.global.event.PushToMembersEvent;
import com._2cha.demo.push.dto.Payload;
import com._2cha.demo.push.dto.PayloadWithoutTarget;
import com._2cha.demo.push.dto.PushResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

//TODO: separate transaction and api call
// - manage tokens with timestamp (last active time)
// - detach API call from transaction
public interface PushService {

  void register(Long memberId, String sub);

  void unregister(Long memberId, String sub);

  void unregisterAll(Long memberId);

  PushResponse subscribe(Long memberId, String topic);

  CompletableFuture<PushResponse> subscribeAsync(Long memberId, String topic);

  PushResponse unsubscribe(Long memberId, String topic);

  CompletableFuture<PushResponse> unsubscribeAsync(Long memberId, String topic);

  PushResponse send(Payload payload);

  PushResponse sendToMembers(List<Long> memberIds, PayloadWithoutTarget payload);

  CompletableFuture<PushResponse> handlePushEvent(PushEvent event);

  CompletableFuture<PushResponse> handlePushToMemberEvent(PushToMembersEvent event);

  CompletableFuture<PushResponse> sendAsync(Payload payload);

  CompletableFuture<PushResponse> sendToMembersAsync(List<Long> memberIds,
                                                     PayloadWithoutTarget payload);

  CompletableFuture<PushResponse> sendToMembersAsync(List<Long> memberIds,
                                                     String title, String body, Object data);
}
