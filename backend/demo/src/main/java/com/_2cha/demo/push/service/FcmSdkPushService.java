package com._2cha.demo.push.service;

import com._2cha.demo.member.domain.Member;
import com._2cha.demo.member.exception.NoSuchMemberException;
import com._2cha.demo.member.service.MemberService;
import com._2cha.demo.push.domain.PushSubject;
import com._2cha.demo.push.dto.Payload;
import com._2cha.demo.push.dto.PayloadWithoutTarget;
import com._2cha.demo.push.dto.PushResponse;
import com._2cha.demo.push.exception.CannotUnregisterException;
import com._2cha.demo.push.exception.InvalidSubjectException;
import com._2cha.demo.push.exception.NoRegisteredSubjectException;
import com._2cha.demo.push.repository.PushSubjectRepository;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.SendResponse;
import com.google.firebase.messaging.TopicManagementResponse;
import com.google.firebase.messaging.TopicManagementResponse.Error;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Firebase Admin SDK ver
 */
@Slf4j
@Service
@Transactional
public class FcmSdkPushService implements PushService {

  private final ServiceAccountCredentials cred;
  private final PushSubjectRepository pushSubjectRepository;
  private final MemberService memberService;

  private final FirebaseMessaging fcm;

  public FcmSdkPushService(ServiceAccountCredentials cred,
                           PushSubjectRepository pushSubjectRepository,
                           MemberService memberService) {
    this.cred = cred;
    this.pushSubjectRepository = pushSubjectRepository;
    this.memberService = memberService;

    FirebaseOptions options = FirebaseOptions.builder()
                                             .setCredentials(this.cred)
                                             .build();
    FirebaseApp.initializeApp(options);
    this.fcm = FirebaseMessaging.getInstance();
  }

  /*-----------
   @ Commands
   ----------*/
  @Override
  public void register(Long memberId, String sub) {
    Member member = this.memberService.findById(memberId);
    if (member == null) throw new NoSuchMemberException();
    if (!validateFcmToken(sub)) throw new InvalidSubjectException();

    pushSubjectRepository.save(PushSubject.register(member, sub));
  }

  @Override
  public void unregister(Long memberId, String sub) {
    PushSubject pushSubject = pushSubjectRepository.findByValue(sub);
    if (pushSubject == null) throw new NoRegisteredSubjectException();
    if (!Objects.equals(pushSubject.getMember().getId(), memberId)) {
      throw new CannotUnregisterException();
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
  public PushResponse subscribeToTopic(Long memberId, String topic) {
    List<PushSubject> pushSubjects = pushSubjectRepository.findAllByMemberId(memberId);
    if (pushSubjects.isEmpty()) throw new NoRegisteredSubjectException();

    List<String> tokens = pushSubjects.stream()
                                      .map(PushSubject::getValue)
                                      .toList();
    FcmOpResult result = executeTopicOp(fcm::subscribeToTopic, tokens, topic);

    return new PushResponse(tokens.size(),
                            result.getSuccessCount(),
                            result.getFailureCount(),
                            result.getFailures());
  }

  @Override
  public PushResponse unsubscribeFromTopic(Long memberId, String topic) {
    List<PushSubject> pushSubjects = pushSubjectRepository.findAllByMemberId(memberId);
    if (pushSubjects.isEmpty()) throw new NoRegisteredSubjectException();

    List<String> tokens = pushSubjects.stream()
                                      .map(PushSubject::getValue)
                                      .toList();
    FcmOpResult result = executeTopicOp(fcm::unsubscribeFromTopic, tokens, topic);

    return new PushResponse(tokens.size(),
                            result.getSuccessCount(),
                            result.getFailureCount(),
                            result.getFailures());
  }

  /*-----------
   @ Queries
   ----------*/
  @Override
  public PushResponse send(Payload payload) {
    FcmOpResult result = executeUnitOp(fcm::send, payload);
    return new PushResponse(1,
                            result.getSuccessCount(),
                            result.getFailureCount(),
                            result.getFailures());
  }


  @Override
  public PushResponse sendToMembers(List<Long> memberIds, PayloadWithoutTarget payload) {
    List<PushSubject> allSubjects = pushSubjectRepository.findAllByMemberIdIn(memberIds);
    if (allSubjects.isEmpty()) return new PushResponse(0, 0, 0, Collections.emptyList());

    FcmOpResult result = executeMulticastOp(fcm::sendMulticast,
                                            allSubjects.stream()
                                                       .map(PushSubject::getValue)
                                                       .toList(),
                                            payload);
    return new PushResponse(allSubjects.size(),
                            result.getSuccessCount(),
                            result.getFailureCount(),
                            result.getFailures());
  }


  /*-----------
   @ Others
   ----------*/
  private boolean validateFcmToken(String sub) {
    try {
      fcm.send(buildMessage(new Payload(sub, null, null, "title", "body", null)), true); // dry-run
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

  private MulticastMessage buildMulticastMessage(List<String> subjects,
                                                 PayloadWithoutTarget payload) {

    return MulticastMessage.builder()
                           .addAllTokens(subjects)
                           .setNotification(Notification.builder()
                                                        .setTitle(payload.getTitle())
                                                        .setBody(payload.getBody())
                                                        .setImage("https://picsum.photos/200/200")
                                                        .build())
                           .build();
  }

  private boolean needsRemoval(FirebaseMessagingException ex) {
    return switch (ex.getMessagingErrorCode()) {
      case INVALID_ARGUMENT ->
          ex.getMessage().equals("The registration token is not a valid FCM registration token");
      case UNREGISTERED -> true; /* Remove record */
      default -> false;
    };
  }

  private boolean needsRemoval(Error error) {
    return switch (error.getReason()) {
      case "registration-token-not-registered",
          /* NOTE: No specific error about this code, but no possibilities other than */
          "invalid-argument" -> true;
      default -> false;
    };
  }

  private FcmOpResult executeUnitOp(UnitFunction executor, Payload payload) {
    Message message = buildMessage(payload);
    List<FailedOp> failedOps = new ArrayList<>();
    String code = null;
    int success = 0;
    int failure = 0;
    try {
      executor.run(message);
      success += 1;
    } catch (FirebaseMessagingException e) {
      if (needsRemoval(e)) {
        code = "NEEDS_REMOVAL";
      } else {
        code = e.getMessagingErrorCode().toString();
      }
      failure += 1;
      failedOps.add(new FailedOp(payload.getTarget(), code));
    }
    return new FcmOpResult(success, failure, failedOps);
  }

  private FcmOpResult executeTopicOp(TopicFunction executor, List<String> tokens,
                                     String topic) {
    List<Error> errors = Collections.emptyList();
    List<FailedOp> failedOps = new ArrayList<>();
    Integer successCount = 0;
    Integer failureCount = 0;
    try {
      TopicManagementResponse response = executor.run(tokens, topic);
      successCount = response.getSuccessCount();
      failureCount = response.getFailureCount();
      errors = response.getErrors();
    } catch (FirebaseMessagingException e) {
      /* may I/O Exception */
      //TODO: wrap and throw
      log.error("{}", e);
    }
    for (var error : errors) {
      String token = tokens.get(error.getIndex());
      if (needsRemoval(error)) {
        failedOps.add(new FailedOp(token, "NEED_REMOVAL"));
      } else {
        failedOps.add(new FailedOp(token, error.getReason().toUpperCase().replaceAll("-", "_")));
      }
    }
    return new FcmOpResult(successCount, failureCount, failedOps);
  }


  private FcmOpResult executeMulticastOp(MulticastFunction executor, List<String> tokens,
                                         PayloadWithoutTarget payload) {
    List<FailedOp> failedOps = new ArrayList<>();
    Integer successCount = 0;
    Integer failureCount = 0;

    List<SendResponse> responses = Collections.emptyList();
    try {
      MulticastMessage message = buildMulticastMessage(tokens, payload);
      BatchResponse ret = executor.run(message);
      responses = ret.getResponses();
      successCount = ret.getSuccessCount();
      failureCount = ret.getFailureCount();
    } catch (FirebaseMessagingException e) {
      /* may I/O Exception */
      //TODO: wrap and throw
      log.error("{}", e);
    }
    ListIterator<SendResponse> it = responses.listIterator();
    while (it.hasNext()) {
      var idx = it.nextIndex();
      var res = it.next();
      var token = tokens.get(idx);
      if (res.isSuccessful()) continue;
      if (needsRemoval(res.getException())) {
        failedOps.add(new FailedOp(token, "NEEDS_REMOVAL"));
      } else {
        failedOps.add(new FailedOp(token, res.getException().getMessagingErrorCode().toString()));
      }
    }
    return new FcmOpResult(successCount, failureCount, failedOps);
  }
}

@FunctionalInterface
interface UnitFunction {

  void run(Message message) throws FirebaseMessagingException;
}

@FunctionalInterface
interface TopicFunction {

  TopicManagementResponse run(List<String> tokens, String topic) throws FirebaseMessagingException;
}

@FunctionalInterface
interface MulticastFunction {

  BatchResponse run(MulticastMessage message) throws FirebaseMessagingException;
}
