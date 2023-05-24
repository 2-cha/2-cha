package com._2cha.demo.push.controller;

import com._2cha.demo.global.annotation.Auth;
import com._2cha.demo.global.annotation.Authed;
import com._2cha.demo.member.domain.Role;
import com._2cha.demo.push.dto.Payload;
import com._2cha.demo.push.dto.PushRegistrationRequest;
import com._2cha.demo.push.dto.PushResponse;
import com._2cha.demo.push.service.PushService;
import com._2cha.demo.push.validator.TopicName;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class PushController {

  private final PushService pushService;

  @Auth(Role.ADMIN)
  @PostMapping("/send")
  public PushResponse send(@RequestBody @Valid Payload payload) {
    return this.pushService.send(payload);
  }

  @Auth
  @PostMapping("/push/registration")
  public void register(@Authed Long memberId, @RequestBody @Valid PushRegistrationRequest dto) {
    this.pushService.register(memberId, dto.getSubject());
  }

  @Auth
  @DeleteMapping("/push/registration/{subject}")
  public void unregister(@Authed Long memberId, @PathVariable String subject) {
    this.pushService.unregister(memberId, subject);
  }

  @Auth
  @DeleteMapping("/push/registration/all")
  public void unregisterAll(@Authed Long memberId) {
    this.pushService.unregisterAll(memberId);
  }

  @Auth
  @PostMapping("/push/subscription")
  public PushResponse subscribeToTopic(@Authed Long memberId,
                                       @RequestParam @TopicName String topic) {
    return this.pushService.subscribeToTopic(memberId, topic);
  }

  @Auth
  @DeleteMapping("/push/subscription")
  public PushResponse unsubscribeFromTopic(@Authed Long memberId,
                                           @RequestParam @TopicName String topic) {
    return this.pushService.unsubscribeFromTopic(memberId, topic);
  }
}