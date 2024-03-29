package com._2cha.demo.member.service;

import com._2cha.demo.global.event.MemberDeletedEvent;
import com._2cha.demo.global.infra.imageupload.service.ImageUploadService;
import com._2cha.demo.global.infra.storage.service.FileStorageService;
import com._2cha.demo.member.domain.Achievement;
import com._2cha.demo.member.domain.Member;
import com._2cha.demo.member.domain.MemberAchievement;
import com._2cha.demo.member.domain.OIDCProvider;
import com._2cha.demo.member.dto.AchievementResponse;
import com._2cha.demo.member.dto.MemberCredResponse;
import com._2cha.demo.member.dto.MemberInfoResponse;
import com._2cha.demo.member.dto.MemberProfileResponse;
import com._2cha.demo.member.dto.RelationshipOperationResponse;
import com._2cha.demo.member.dto.ToggleAchievementExposureResponse;
import com._2cha.demo.member.event.ProfileImageUpdateRequiredEvent;
import com._2cha.demo.member.event.ProfileImageUploadRequiredEvent;
import com._2cha.demo.member.exception.DuplicatedNameException;
import com._2cha.demo.member.exception.NoSuchAchievementException;
import com._2cha.demo.member.exception.NoSuchMemberException;
import com._2cha.demo.member.repository.AchievementRepository;
import com._2cha.demo.member.repository.MemberQueryRepository;
import com._2cha.demo.member.repository.MemberRepository;
import com._2cha.demo.push.service.PushService;
import com._2cha.demo.util.BCryptHashingUtils;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

  private final MemberRepository memberRepository;
  private final MemberQueryRepository memberQueryRepository;
  private final AchievementRepository achvRepository;
  private final FileStorageService fileStorageService;
  private final ImageUploadService imageUploadService;
  private final ApplicationEventPublisher eventPublisher;
  private final NicknameGeneratorService nicknameGeneratorService;
  private final PushService pushService;


  public Member findById(Long id) {
    Member member = memberRepository.findById(id);
    if (member == null) throw new NoSuchMemberException();

    return member;
  }

  /*------------
   @ Commands
   ------------*/
  @Transactional
  public MemberInfoResponse signUp(String email, String name, String password) {
    String hashed = BCryptHashingUtils.hash(password);

    Member member = Member.createMember(email, hashed, name);
    memberRepository.save(member);

    return new MemberInfoResponse(member);
  }

  @Async("imageUploadTaskExecutor")
  @TransactionalEventListener(value = ProfileImageUploadRequiredEvent.class, phase = TransactionPhase.AFTER_COMMIT)
  public void uploadProfileImage(ProfileImageUploadRequiredEvent event)
      throws InterruptedException, ExecutionException {
    String srcImgUrl = event.getSourceImageUrl();

    if (srcImgUrl != null) {
      try (var in = new URL(srcImgUrl).openStream()) {
        byte[] imageBytes = in.readAllBytes();
        String savedThumbnailUrl = imageUploadService.saveThumbnail(imageBytes).get().getUrl();
        String savedThumbUrlPath = fileStorageService.extractPath(savedThumbnailUrl);
        eventPublisher.publishEvent(new ProfileImageUpdateRequiredEvent(this,
                                                                        event.getMemberId(),
                                                                        savedThumbUrlPath));
      } catch (IOException e) {
        //TODO: handle in SimpleAsyncUncaughtExceptionHandler (Not in request lifecycle)
        return;
      }
    }
  }

  @Async("imageUploadTaskExecutor")
  @Transactional
  @EventListener(value = ProfileImageUpdateRequiredEvent.class)
  public void updateProfileImage(ProfileImageUpdateRequiredEvent event) {
    Member member = memberRepository.findById(event.getMemberId());
    member.updateProfileImage(event.getThumbUrlPath());
    memberRepository.save(member);
  }

  @Transactional
  public MemberProfileResponse updateProfileImage(Long memberId, String url) {
    Member member = memberRepository.findById(memberId);
    String thumbUrlPath = fileStorageService.extractPath(url);

    member.updateProfileImage(thumbUrlPath);
    memberRepository.save(member);

    return new MemberProfileResponse(member.getId(),
                                     member.getName(),
                                     member.getProfImgThumbPath(),
                                     member.getProfMsg(),
                                     fileStorageService.getBaseUrl());
  }

  @Transactional
  public MemberInfoResponse signUpWithOIDC(OIDCProvider oidcProvider, String oidcId,
                                           String name, String email,
                                           String srcImgUrl
                                          ) {
    final int retry = 10;
    String nickname = name + "_" + UUID.randomUUID();
    for (int i = 0; i < retry; i++) {

      List<String> nicknamePool = nicknameGeneratorService.generate(100);
      List<Member> members = memberRepository.findAllByNameIn(nicknamePool);
      Optional<String> res = nicknamePool.stream()
                                         .filter(n -> members.stream()
                                                             .noneMatch(m -> m.getName().equals(n)))
                                         .findFirst();
      if (res.isPresent()) {
        nickname = res.get();
        break;
      }
    }

    Member member = Member.createMemberWithOIDC(oidcProvider, oidcId, email, nickname, null);
    memberRepository.save(member);
    eventPublisher.publishEvent(
        new ProfileImageUploadRequiredEvent(this, member.getId(), srcImgUrl));

    return new MemberInfoResponse(member);
  }

  @Transactional
  public RelationshipOperationResponse follow(Long followerId, Long followingId) {
    Member follower = memberRepository.findById(followerId);
    Member following = memberRepository.findById(followingId);
    if (follower == null || following == null) throw new NoSuchMemberException();

    follower.follow(following);

//    if (following.getFollowers().size() == 1) {
//      addAchievement(followingId, 1L);
//    }

    return new RelationshipOperationResponse(followerId, followingId);
  }

  @Transactional
  public MemberProfileResponse updateProfile(Long memberId, String name, String profMsg) {
    Member member = memberRepository.findById(memberId);
    if (member == null) throw new NoSuchMemberException();
    if (!Objects.equals(member.getName(), name) &&
        memberRepository.findByName(name) != null) {throw new DuplicatedNameException();}

    member.updateProfile(name, profMsg);
    return new MemberProfileResponse(member.getId(),
                                     member.getName(),
                                     member.getProfImgThumbPath(),
                                     member.getProfMsg(),
                                     fileStorageService.getBaseUrl());
  }

  @Transactional
  public RelationshipOperationResponse unfollow(Long followerId, Long followingId) {
    Member follower = memberRepository.findById(followerId);
    Member following = memberRepository.findById(followingId);
    if (follower == null || following == null) throw new NoSuchMemberException();

    follower.unfollow(following);

    return new RelationshipOperationResponse(followerId, followingId);
  }

  @Transactional
  public void addAchievement(Long memberId, Long achvId) {
    Achievement achievement = achvRepository.findAchievementById(achvId);
    if (achievement == null) throw new NoSuchAchievementException();

    Member member = memberRepository.findById(memberId);
    if (member == null) throw new NoSuchMemberException();

    member.addAchievement(achievement);
  }

  @Transactional
  public List<ToggleAchievementExposureResponse> toggleAchievementsExposure(Long memberId,
                                                                            Map<Long, Boolean> achvExposureMap) {
    Member member = memberRepository.findById(memberId);
    if (member == null) throw new NoSuchMemberException();

    List<MemberAchievement> memAchvs = member.getAchievements();

    List<ToggleAchievementExposureResponse> response = new ArrayList<>();
    memAchvs.forEach(
        memAchv -> {
          Long achvId = memAchv.getAchievement().getId();
          if (achvExposureMap.containsKey(achvId)) {
            memAchv.toggleExposure(achvExposureMap.get(achvId));
            response.add(new ToggleAchievementExposureResponse(achvId, memAchv.isExposed()));
          }
        });
    return response;
  }

  @Transactional
  public void deleteMember(Long memberId) {
    Member member = memberRepository.findById(memberId);
    if (member == null) throw new NoSuchMemberException();

    pushService.unregisterAll(memberId);
    member.getAchievements().forEach(member::removeAchievement);
    member.getFollowings().forEach(rel -> member.unfollow(rel.getFollowing()));
    member.getFollowers().forEach(rel -> rel.getFollower().unfollow(member));
    member.softDelete();

    memberRepository.save(member);
    eventPublisher.publishEvent(new MemberDeletedEvent(this, memberId));
  }


  /*------------
   @ Queries
   ------------*/
  @Transactional(readOnly = true, noRollbackFor = {NoSuchMemberException.class})
  public MemberProfileResponse getMemberProfileById(Long id) {

    MemberProfileResponse profile = memberQueryRepository.getMemberProfileById(id,
                                                                               fileStorageService.getBaseUrl());
    if (profile == null) throw new NoSuchMemberException();

    return profile;
  }

  public List<MemberProfileResponse> getMemberProfileByIdIn(List<Long> ids) {
    return memberQueryRepository.getMemberProfileByIdIn(ids, fileStorageService.getBaseUrl());
  }

  @Transactional(readOnly = true, noRollbackFor = {NoSuchMemberException.class})
  public MemberInfoResponse getMemberInfoById(Long id) {
    MemberInfoResponse memberInfo = memberQueryRepository.getMemberInfoById(id,
                                                                            fileStorageService.getBaseUrl());
    if (memberInfo == null) throw new NoSuchMemberException();

    return memberInfo;
  }

  @Transactional(readOnly = true, noRollbackFor = {NoSuchMemberException.class})
  public MemberInfoResponse getMemberInfoByOidcId(OIDCProvider oidcProvider,
                                                  String oidcId) {
    MemberInfoResponse memberInfo = memberQueryRepository.getMemberInfoByOidcId(
        oidcProvider, oidcId, fileStorageService.getBaseUrl());
    if (memberInfo == null) throw new NoSuchMemberException();

    return memberInfo;
  }

  @Transactional(readOnly = true, noRollbackFor = {NoSuchMemberException.class})
  public MemberCredResponse getMemberCredByEmail(String email) {
    MemberCredResponse memberCred = memberQueryRepository.getMemberCredByEmail(email);
    if (memberCred == null) throw new NoSuchMemberException();

    return memberCred;
  }

  public List<MemberProfileResponse> getFollowers(Long memberId) {
    return memberQueryRepository.getFollowersProfiles(memberId, fileStorageService.getBaseUrl());
  }

  public List<MemberProfileResponse> getFollowings(Long memberId) {
    return memberQueryRepository.getFollowingsProfiles(memberId, fileStorageService.getBaseUrl());
  }

  public List<AchievementResponse> getAllMemberAchievements(Long memberId) {
    return memberQueryRepository.getMemberAchievements(memberId, false);
  }

  public List<AchievementResponse> getExposedMemberAchievements(Long memberId) {
    return memberQueryRepository.getMemberAchievements(memberId, true);
  }
}