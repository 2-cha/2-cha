package com._2cha.demo.push.controller;

import com._2cha.demo.global.annotation.Auth;
import com._2cha.demo.global.annotation.Authed;
import com._2cha.demo.member.domain.Role;
import com._2cha.demo.push.dto.Payload;
import com._2cha.demo.push.dto.PushRegistrationRequest;
import com._2cha.demo.push.service.PushService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PushController {

  private final PushService pushService;

  @Auth(Role.ADMIN)
  @PostMapping("/send")
  public void send(@RequestBody @Valid Payload payload) {
    this.pushService.send(payload);
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
  public void subscribeToTopic(@Authed Long memberId,
                               @RequestParam String topic) {
    this.pushService.subscribeToTopic(memberId, topic);
  }

  @Auth
  @DeleteMapping("/push/subscription")
  public void unsubscribeFromTopic(@Authed Long memberId,
                                   @RequestParam String topic) {
    this.pushService.unsubscribeFromTopic(memberId, topic);
  }
}