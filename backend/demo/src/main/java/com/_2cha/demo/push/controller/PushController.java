package com._2cha.demo.push.controller;

import com._2cha.demo.global.annotation.Auth;
import com._2cha.demo.global.annotation.Authed;
import com._2cha.demo.push.dto.Payload;
import com._2cha.demo.push.dto.PushRegistrationRequest;
import com._2cha.demo.push.dto.PushResponse;
import com._2cha.demo.push.dto.SendToMembersRequest;
import com._2cha.demo.push.service.PushService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class PushController {

  private final PushService pushService;

  //  @Auth(Role.ADMIN)
  @PostMapping("/send")
  public PushResponse send(@RequestBody @Valid Payload payload) {
    return this.pushService.send(payload);
  }

  //  @Auth(Role.ADMIN)
  @PostMapping("/sendto")
  public PushResponse sendToMembers(@RequestBody @Valid SendToMembersRequest dto) {
    return this.pushService.sendToMembers(dto.getMemberIds(), dto.getPayload());
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
  @PutMapping("/push/activity/{subject}")
  public void updateActivity(@Authed Long memberId, @PathVariable String subject) {
    this.pushService.updateActivity(memberId, subject);
  }
}