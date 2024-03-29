package com._2cha.demo.push.service;

//import com._2cha.demo.global.event.PushEvent;
//import com._2cha.demo.global.event.PushToMembersEvent;
//import com._2cha.demo.member.domain.Member;
//import com._2cha.demo.member.exception.NoSuchMemberException;
//import com._2cha.demo.member.service.MemberService;
//import com._2cha.demo.push.config.FcmOAuth2Config;
//import com._2cha.demo.push.domain.PushSubject;
//import com._2cha.demo.push.dto.Payload;
//import com._2cha.demo.push.dto.PayloadWithoutTarget;
//import com._2cha.demo.push.dto.PushResponse;
//import com._2cha.demo.push.repository.PushSubjectRepository;
//import com.google.auth.oauth2.ServiceAccountCredentials;
//import java.io.IOException;
//import java.util.List;
//import java.util.Objects;
//import java.util.concurrent.CompletableFuture;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.MediaType;
//import org.springframework.web.reactive.function.client.WebClient;
//
//@Slf4j
//@RequiredArgsConstructor
//public class FcmHttpV1PushService implements PushService {
//
//  private final ServiceAccountCredentials cred;
//  private final PushSubjectRepository pushSubjectRepository;
//  private final MemberService memberService;
//  private final FcmOAuth2Config config;
//
//  private static final String BASE_URL = "https://fcm.googleapis.com";
//
//  /**
//   * FCM HTTP v1 API ver
//   *
//   * @return
//   */
//  @Override
//  public PushResponse send(Payload payload) {
//    final String FCM_SEND_ENDPOINT = "/v1/projects/" + config.getProjectId() + "/messages:send";
//    WebClient.create(BASE_URL + FCM_SEND_ENDPOINT)
//             .post()
//             .header("Authorization", "Bearer " + getAccessToken())
//             .contentType(MediaType.APPLICATION_JSON)
//             .bodyValue(payload)
//             .retrieve()
//             .bodyToMono(String.class)
//             .block();
//    return null;
//  }
//
//  @Override
//  public void register(Long memberId, String sub) {
//    Member member = this.memberService.findById(memberId);
//    if (member == null) throw new NoSuchMemberException();
////    if (!validateFcmToken(sub)) throw new BadRequestException("Invalid FCM token.");
//
//    pushSubjectRepository.save(PushSubject.register(member, sub));
//  }
//
//  @Override
//  public void unregister(Long memberId, String sub) {
//    PushSubject pushSubject = pushSubjectRepository.findByValue(sub);
//    if (pushSubject == null) throw new RuntimeException("Not Found: Not registered");
//    if (!Objects.equals(pushSubject.getMember().getId(), memberId)) {
//      throw new RuntimeException("Forbidden: Not an owner");
//    }
//
//    pushSubjectRepository.delete(pushSubject);
//  }
//
//  @Override
//  public void unregisterAll(Long memberId) {
//    List<PushSubject> pushSubjects = pushSubjectRepository.findAllByMemberId(memberId);
//    if (pushSubjects.isEmpty()) return;
//
//    pushSubjectRepository.deleteAllInBatch(pushSubjects);
//  }
//
//  @Override
//  public PushResponse subscribeToTopicForMember(Long memberId, String topic) {
//
//    return null;
//  }
//
//  @Override
//  public PushResponse unsubscribeFromTopicForMember(Long memberId, String topic) {
//
//    return null;
//  }
//
//  @Override
//  public PushResponse sendToMembers(List<Long> memberIds, PayloadWithoutTarget payload) {
//
//    return null;
//  }
//
//  @Override
//  public CompletableFuture<PushResponse> subscribeToTopicForMemberAsync(Long memberId, String topic) {
//    return null;
//  }
//
//  @Override
//  public CompletableFuture<PushResponse> sendToMembersAsync(List<Long> memberIds, String title,
//                                                            String body, Object data) {
//    return null;
//  }
//
//  @Override
//  public CompletableFuture<PushResponse> unsubscribeFromTopicForMemberAsync(Long memberId, String topic) {
//    return null;
//  }
//
//  @Override
//  public CompletableFuture<PushResponse> handlePushEvent(PushEvent event) {
//    return null;
//  }
//
//  @Override
//  public CompletableFuture<PushResponse> handlePushToMemberEvent(PushToMembersEvent event) {
//    return null;
//  }
//
//  @Override
//  public CompletableFuture<PushResponse> sendAsync(Payload payload) {
//    return null;
//  }
//
//  @Override
//  public CompletableFuture<PushResponse> sendToMembersAsync(List<Long> memberIds,
//                                                            PayloadWithoutTarget payload) {
//    return null;
//  }
//
//  private String getAccessToken() {
//    try {
//      cred.refreshIfExpired();
//    } catch (IOException e) {
//      log.error("Unable to refresh FCM OAuth2 token.");
//    }
//    return cred.getAccessToken().getTokenValue();
//  }
//}
