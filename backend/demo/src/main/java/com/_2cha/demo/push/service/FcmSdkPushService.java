package com._2cha.demo.push.service;

import static java.util.Collections.singletonList;
import static java.util.concurrent.CompletableFuture.completedFuture;

import com._2cha.demo.global.event.PushEvent;
import com._2cha.demo.global.event.PushToMembersEvent;
import com._2cha.demo.member.domain.Member;
import com._2cha.demo.member.exception.NoSuchMemberException;
import com._2cha.demo.member.repository.MemberRepository;
import com._2cha.demo.push.domain.PushSubject;
import com._2cha.demo.push.domain.PushTopicSubscription;
import com._2cha.demo.push.dto.Payload;
import com._2cha.demo.push.dto.PayloadWithoutTarget;
import com._2cha.demo.push.dto.PushResponse;
import com._2cha.demo.push.exception.CannotUnregisterException;
import com._2cha.demo.push.exception.CannotUpdateException;
import com._2cha.demo.push.exception.InvalidSubjectException;
import com._2cha.demo.push.exception.NoRegisteredSubjectException;
import com._2cha.demo.push.repository.PushSubjectRepository;
import com._2cha.demo.push.repository.PushTopicSubscriptionRepository;
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
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Firebase Admin SDK ver
 */
@Slf4j
@Service
public class FcmSdkPushService implements PushService {

  private final ServiceAccountCredentials cred;
  private final PushSubjectRepository pushSubjectRepository;
  private final PushTopicSubscriptionRepository topicSubscriptionRepository;
  private final MemberRepository memberRepository;
  private final TransactionTemplate tx;

  private final FirebaseMessaging fcm;

  public FcmSdkPushService(ServiceAccountCredentials cred,
                           PushSubjectRepository pushSubjectRepository,
                           PushTopicSubscriptionRepository topicSubscriptionRepository,
                           MemberRepository memberRepository,
                           TransactionTemplate tx) {
    this.cred = cred;
    this.pushSubjectRepository = pushSubjectRepository;
    this.topicSubscriptionRepository = topicSubscriptionRepository;
    this.memberRepository = memberRepository;
    this.tx = tx;

    FirebaseOptions options = FirebaseOptions.builder()
                                             .setCredentials(this.cred)
                                             .build();
    FirebaseApp.initializeApp(options);
    this.fcm = FirebaseMessaging.getInstance();
  }

  /*-----------
   @ Commands
   ----------*/

  /**
   * Map provided token with member as {@link com._2cha.demo.push.domain.PushSubject}.
   * <p>
   * If member has subscriptions on some topics, registered token will be attached to them.
   */
  @Override
  public void register(Long memberId, String sub) {
    if (!validateFcmToken(sub)) throw new InvalidSubjectException();

    List<PushTopicSubscription> subscriptions = new ArrayList<>();
    tx.executeWithoutResult(status -> {
      Member member = this.memberRepository.findById(memberId);
      if (member == null) throw new NoSuchMemberException();
      pushSubjectRepository.save(PushSubject.register(member, sub));
      subscriptions.addAll(topicSubscriptionRepository.findAllByMemberId(memberId));
    });

    // restore topic subscriptions
    subscriptions.forEach(subscription -> attachTokensToTopic(singletonList(sub),
                                                              subscription.getTopic()));
  }

  /**
   * Unmap provided token with member, by removing {@link com._2cha.demo.push.domain.PushSubject}.
   * <p>
   * If member has subscriptions on some topics, unmapped token will be detached from them.
   */
  @Override
  public void unregister(Long memberId, String sub) {
    List<PushTopicSubscription> subscriptions = new ArrayList<>();
    tx.executeWithoutResult(status -> {
      PushSubject pushSubject = pushSubjectRepository.findByValue(sub);
      if (pushSubject == null) throw new NoRegisteredSubjectException();
      if (!Objects.equals(pushSubject.getMember().getId(), memberId)) {
        throw new CannotUnregisterException();
      }
      subscriptions.addAll(topicSubscriptionRepository.findAllByMemberId(memberId));
      pushSubjectRepository.delete(pushSubject);
    });

    // detach topic subscriptions
    subscriptions.forEach(
        subscription -> detachTokensFromTopic(singletonList(sub), subscription.getTopic()));
  }

  /**
   * NOTE: is it needed?
   */
  @Override
  @Transactional
  public void unregisterAll(Long memberId) {
    List<PushSubject> pushSubjects = pushSubjectRepository.findAllByMemberId(memberId);
    if (pushSubjects.isEmpty()) return;
    List<PushTopicSubscription> subscriptions = topicSubscriptionRepository.findAllByMemberId(
        memberId);

    // remove all topic subscriptions for all tokens
    subscriptions.forEach(
        subscription -> unsubscribe(memberId, subscription.getTopic())
                         );

    pushSubjectRepository.deleteAllInBatch(pushSubjects);
  }


  /**
   * Make subscription for all tokens that are related to member.
   * <p>
   * It may create {@link com._2cha.demo.push.domain.PushTopicSubscription} per member, if not
   * exists.
   */
  @Override
  public PushResponse subscribe(Long memberId, String topic) {
    List<PushSubject> pushSubjects = new ArrayList<>();
    tx.executeWithoutResult(status -> {
      Member member = memberRepository.findById(memberId);
      if (member == null) throw new NoSuchMemberException();

      pushSubjects.addAll(pushSubjectRepository.findAllByMemberId(memberId));
      if (pushSubjects.isEmpty()) throw new NoRegisteredSubjectException();

      // save subscription info to restore on registration of new token for member
      PushTopicSubscription subscription = topicSubscriptionRepository.findByMemberIdAndTopic(
          memberId, topic);
      if (subscription == null) {
        topicSubscriptionRepository.save(PushTopicSubscription.makeSubscription(member, topic));
      }
    });

    List<String> tokens = pushSubjects.stream()
                                      .map(PushSubject::getValue)
                                      .toList();
    return attachTokensToTopic(tokens, topic);
  }

  /**
   * Make subscription for all tokens that are related to member.
   * <p>
   * It may create {@link com._2cha.demo.push.domain.PushTopicSubscription} per member, if not
   * exists.
   */
  @Override
  @Async("pushTaskExecutor")
  public CompletableFuture<PushResponse> subscribeAsync(Long memberId, String topic) {
    return completedFuture(subscribe(memberId, topic));
  }


  /**
   * Cancel subscription for all tokens that are related to member.
   * <p>
   * It removes {@link com._2cha.demo.push.domain.PushTopicSubscription} per member.
   */
  @Override
  public PushResponse unsubscribe(Long memberId, String topic) {
    List<PushSubject> pushSubjects = new ArrayList<>();
    tx.executeWithoutResult(status -> {
      Member member = memberRepository.findById(memberId);
      if (member == null) throw new NoSuchMemberException();

      pushSubjects.addAll(pushSubjectRepository.findAllByMemberId(memberId));
      if (pushSubjects.isEmpty()) throw new NoRegisteredSubjectException();

      // remove all stored subscription info
      topicSubscriptionRepository.deleteAllByMemberIdAndTopic(memberId, topic);
    });

    List<String> tokens = pushSubjects.stream()
                                      .map(PushSubject::getValue)
                                      .toList();
    return detachTokensFromTopic(tokens, topic);
  }

  /**
   * Cancel subscription for all tokens that are related to member.
   * <p>
   * It removes {@link com._2cha.demo.push.domain.PushTopicSubscription} per member.
   */
  @Override
  @Async("pushTaskExecutor")
  public CompletableFuture<PushResponse> unsubscribeAsync(Long memberId, String topic) {
    return completedFuture(unsubscribe(memberId, topic));
  }


  /**
   * Update {@link com._2cha.demo.push.domain.PushSubject#lastActiveTime}.
   */
  @Override
  @Transactional
  public void updateActivity(Long memberId, String token) {
    Member member = memberRepository.findById(memberId);
    if (member == null) throw new NoSuchMemberException();

    PushSubject subject = pushSubjectRepository.findByValue(token);
    if (subject == null) throw new NoRegisteredSubjectException();
    if (!Objects.equals(subject.getMember().getId(), memberId)) throw new CannotUpdateException();

    subject.updateActivity();
  }


  /*-----------
   @ Queries
   ----------*/
  @Override
  @Transactional(readOnly = true)
  public PushResponse sendToMembers(List<Long> memberIds, PayloadWithoutTarget payload) {
    List<PushSubject> allSubjects = pushSubjectRepository.findAllByMemberIdIn(memberIds);
    if (allSubjects.isEmpty()) return new PushResponse(0, 0, 0, Collections.emptyList());

    return sendMulticast(payload, allSubjects.stream()
                                             .map(PushSubject::getValue)
                                             .toList());
  }

  @Override
  @Async("pushTaskExecutor")
  @Transactional(readOnly = true)
  public CompletableFuture<PushResponse> sendToMembersAsync(List<Long> memberIds,
                                                            PayloadWithoutTarget payload) {
    return completedFuture(sendToMembers(memberIds, payload));
  }

  @Override
  @Async("pushTaskExecutor")
  @Transactional(readOnly = true)
  public CompletableFuture<PushResponse> sendToMembersAsync(List<Long> memberIds,
                                                            String title, String body,
                                                            Object data) {
    return completedFuture(sendToMembers(memberIds, new PayloadWithoutTarget(title, body, data)));
  }

  @Override
  @Async("pushTaskExecutor")
  @TransactionalEventListener(value = PushToMembersEvent.class, phase = TransactionPhase.AFTER_COMMIT)
  @Transactional(readOnly = true)
  public CompletableFuture<PushResponse> handlePushToMemberEvent(PushToMembersEvent event) {
    return completedFuture(sendToMembers(event.getReceivers(), event.getPayload()));
  }


  /*-----------
   @ External API
   ----------*/

  /**
   * Make subscription for each registration token.
   * <p>
   * It does not affect {@link com._2cha.demo.push.domain.PushTopicSubscription} per member.
   * <p>
   * Just attach token to FCM push for topic.
   */
  private PushResponse attachTokensToTopic(List<String> tokens, String topic) {
    FcmOpResult result = executeTopicOp(fcm::subscribeToTopic, tokens, topic);

    return new PushResponse(tokens.size(),
                            result.getSuccessCount(),
                            result.getFailureCount(),
                            result.getFailures());
  }

  /**
   * Cancel subscription for each registration token.
   * <p>
   * It does not affect {@link com._2cha.demo.push.domain.PushTopicSubscription} per member.
   * <p>
   * Just detach token from FCM push for topic.
   */
  private PushResponse detachTokensFromTopic(List<String> tokens, String topic) {
    FcmOpResult result = executeTopicOp(fcm::unsubscribeFromTopic, tokens, topic);

    return new PushResponse(tokens.size(),
                            result.getSuccessCount(),
                            result.getFailureCount(),
                            result.getFailures());
  }

  @Override
  public PushResponse send(Payload payload) {
    FcmOpResult result = executeUnitOp(fcm::send, payload);
    return new PushResponse(1,
                            result.getSuccessCount(),
                            result.getFailureCount(),
                            result.getFailures());
  }

  private PushResponse sendMulticast(PayloadWithoutTarget payload,
                                     List<String> tokens) {
    FcmOpResult result = executeMulticastOp(fcm::sendMulticast, tokens, payload);
    return new PushResponse(tokens.size(),
                            result.getSuccessCount(),
                            result.getFailureCount(),
                            result.getFailures());
  }

  @Override
  @Async("pushTaskExecutor")
  public CompletableFuture<PushResponse> sendAsync(Payload payload) {
    return completedFuture(send(payload));
  }

  @Override
  @Async("pushTaskExecutor")
  @TransactionalEventListener(value = PushEvent.class, phase = TransactionPhase.AFTER_COMMIT)
  public CompletableFuture<PushResponse> handlePushEvent(PushEvent event) {
    return completedFuture(send(event.getPayload()));
  }

  private boolean validateFcmToken(String sub) {
    try {
      fcm.send(buildMessage(new Payload(sub, null, null, "title", "body", null)), true); // dry-run
    } catch (FirebaseMessagingException e) {
      return false;
    }
    return true;
  }

  /*-----------
   @ Others
   ----------*/
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
