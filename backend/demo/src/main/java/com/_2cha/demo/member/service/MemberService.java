package com._2cha.demo.member.service;

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
import com._2cha.demo.member.exception.NoSuchAchievementException;
import com._2cha.demo.member.exception.NoSuchMemberException;
import com._2cha.demo.member.repository.AchievementRepository;
import com._2cha.demo.member.repository.MemberQueryRepository;
import com._2cha.demo.member.repository.MemberRepository;
import com._2cha.demo.util.BCryptHashingUtils;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  @Transactional
  public MemberInfoResponse signUpWithOIDC(OIDCProvider oidcProvider, String oidcId,
                                           String name, String email,
                                           String orgImgUrl
                                          ) {
    String savedImageUrlPath = null;
    String savedThumbUrlPath = null;
    if (orgImgUrl != null) {
      byte[] imageBytes;
      String savedUrl;
      try (var in = new URL(orgImgUrl).openStream()) {
        imageBytes = in.readAllBytes();
        savedUrl = imageUploadService.save(imageBytes).getUrl();
      } catch (IOException e) {
        throw new RuntimeException(
            "Invalid image url");  //TODO: define and handle exception in async
      }
      savedImageUrlPath = fileStorageService.extractPath(savedUrl);
      savedThumbUrlPath = imageUploadService.getThumbnailPath(savedImageUrlPath);
    }
    Member member = Member.createMemberWithOIDC(oidcProvider, oidcId, email, name,
                                                savedImageUrlPath,
                                                savedThumbUrlPath);
    memberRepository.save(member);

    return new MemberInfoResponse(member);
  }

  @Transactional
  public RelationshipOperationResponse follow(Long followerId, Long followingId) {
    Member follower = memberRepository.findById(followerId);
    Member following = memberRepository.findById(followingId);
    if (follower == null || following == null) throw new NoSuchMemberException();

    follower.follow(following);

    if (following.getFollowers().size() == 1) {
      addAchievement(followingId, 1L);
    }

    return new RelationshipOperationResponse(followerId, followingId);
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


  /*------------
   @ Queries
   ------------*/
  public MemberProfileResponse getMemberProfileById(Long id) {

    MemberProfileResponse profile = memberQueryRepository.getMemberProfileById(id,
                                                                               fileStorageService.getBaseUrl());
    if (profile == null) throw new NoSuchMemberException();

    return profile;
  }

  public List<MemberProfileResponse> getMemberProfileByIdIn(List<Long> ids) {
    return memberQueryRepository.getMemberProfileByIdIn(ids, fileStorageService.getBaseUrl());
  }

  public MemberInfoResponse getMemberInfoById(Long id) {
    MemberInfoResponse memberInfo = memberQueryRepository.getMemberInfoById(id,
                                                                            fileStorageService.getBaseUrl());
    if (memberInfo == null) throw new NoSuchMemberException();

    return memberInfo;
  }

  public MemberInfoResponse getMemberInfoByOidcId(OIDCProvider oidcProvider,
                                                  String oidcId) {
    MemberInfoResponse memberInfo = memberQueryRepository.getMemberInfoByOidcId(
        oidcProvider, oidcId, fileStorageService.getBaseUrl());
    if (memberInfo == null) throw new NoSuchMemberException();

    return memberInfo;
  }

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