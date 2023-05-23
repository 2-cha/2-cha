package com._2cha.demo.push.service;

import com._2cha.demo.global.exception.BadRequestException;
import com._2cha.demo.member.domain.Member;
import com._2cha.demo.member.exception.NoSuchMemberException;
import com._2cha.demo.member.service.MemberService;
import com._2cha.demo.push.domain.PushSubject;
import com._2cha.demo.push.dto.Payload;
import com._2cha.demo.push.repository.PushSubjectRepository;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Firebase Admin SDK ver
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FcmSdkPushService implements PushService {

  private final ServiceAccountCredentials cred;
  private final PushSubjectRepository pushSubjectRepository;
  private final MemberService memberService;

  /*-----------
   @ Commands
   ----------*/
  @Override
  public void register(Long memberId, String sub) {
    Member member = this.memberService.findById(memberId);
    if (member == null) throw new NoSuchMemberException();
    if (!validateFcmToken(sub)) throw new BadRequestException("Invalid FCM token.");

    pushSubjectRepository.save(PushSubject.register(member, sub));
  }

  @Override
  public void unregister(Long memberId, String sub) {
    PushSubject pushSubject = pushSubjectRepository.findByValue(sub);
    if (pushSubject == null) throw new RuntimeException("Not Found: Not registered");
    if (!Objects.equals(pushSubject.getMember().getId(), memberId)) {
      throw new RuntimeException("Forbidden: Not an owner");
    }

    pushSubjectRepository.delete(pushSubject);
  }

  @Override
  public void unregisterAll(Long memberId) {
    List<PushSubject> pushSubjects = pushSubjectRepository.findAllByMemberId(memberId);
    if (pushSubjects.isEmpty()) return;

    pushSubjectRepository.deleteAllInBatch(pushSubjects);
  }

  @Override
  public void subscribeToTopic(Long memberId, String topic) {
    List<PushSubject> pushSubjects = pushSubjectRepository.findAllByMemberId(memberId);
    if (pushSubjects.isEmpty()) throw new RuntimeException("Not Found: No registered token.");

    List<String> tokens = pushSubjects.stream()
                                      .map(PushSubject::getValue)
                                      .toList();
    try {
      FirebaseMessaging.getInstance().subscribeToTopic(tokens, topic);
    } catch (FirebaseMessagingException e) {
      throw new IllegalStateException("FCM Error: " + e.getMessagingErrorCode());
    }
  }

  @Override
  public void unsubscribeFromTopic(Long memberId, String topic) {

    List<PushSubject> pushSubjects = pushSubjectRepository.findAllByMemberId(memberId);
    if (pushSubjects.isEmpty()) throw new RuntimeException("No registered token.");

    List<String> tokens = pushSubjects.stream()
                                      .map(PushSubject::getValue)
                                      .toList();
    try {
      FirebaseMessaging.getInstance().unsubscribeFromTopic(tokens, topic);
    } catch (FirebaseMessagingException e) {
      throw new IllegalStateException("FCM Error: " + e.getMessagingErrorCode());
    }
  }

  /*-----------
   @ Queries
   ----------*/
  @Override
  public void send(Payload payload) {
    Message message = buildMessage(payload);

    try {
      FirebaseMessaging.getInstance().send(message);
    } catch (FirebaseMessagingException e) {
      throw new IllegalStateException("FCM Error: " + e.getMessagingErrorCode());
    }
  }

  /*-----------
   @ Others
   ----------*/
  private boolean validateFcmToken(String sub) {
    try {
      FirebaseMessaging.getInstance()
                       .send(buildMessage(new Payload(sub, null, null, "title", "body", null)),
                             true); // dry-run
    } catch (FirebaseMessagingException e) {
      return false;
    }
    return true;
  }

  private Message buildMessage(Payload payload) {
    return Message.builder()
                  .setToken(payload.getSub())
                  .setTopic(payload.getTopic())
                  .setCondition(payload.getCondition())
                  .setNotification(
                      Notification.builder()
                                  .setTitle(payload.getTitle())
                                  .setBody(payload.getBody())
                                  .setImage("https://picsum.photos/200/200")
                                  .build())
                  .build();
  }

  @PostConstruct
  public void init() {
    FirebaseOptions options = FirebaseOptions.builder()
                                             .setCredentials(this.cred)
                                             .build();
    FirebaseApp.initializeApp(options);
  }
}
