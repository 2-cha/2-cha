package com._2cha.demo.member.service;

import com._2cha.demo.global.exception.NoSuchMemberException;
import com._2cha.demo.member.domain.Achievement;
import com._2cha.demo.member.domain.Member;
import com._2cha.demo.member.domain.MemberAchievement;
import com._2cha.demo.member.domain.OIDCProvider;
import com._2cha.demo.member.dto.AchievementResponse;
import com._2cha.demo.member.dto.MemberCredResponse;
import com._2cha.demo.member.dto.MemberInfoResponse;
import com._2cha.demo.member.dto.MemberProfileResponse;
import com._2cha.demo.member.dto.RelationshipOperationResponse;
import com._2cha.demo.member.dto.SignUpRequest;
import com._2cha.demo.member.dto.ToggleAchievementExposureRequest;
import com._2cha.demo.member.dto.ToggleAchievementExposureResponse;
import com._2cha.demo.member.repository.AchievementRepository;
import com._2cha.demo.member.repository.MemberQueryRepository;
import com._2cha.demo.member.repository.MemberRepository;
import com._2cha.demo.util.BCryptHashingUtils;
import java.util.ArrayList;
import java.util.HashMap;
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


  public Member findById(Long id) {
    Member member = memberRepository.findById(id);
    if (member == null) throw new NoSuchMemberException();

    return member;
  }

  /*------------
   @ Commands
   ------------*/
  @Transactional
  public MemberInfoResponse signUp(SignUpRequest dto) {
    String hashed = BCryptHashingUtils.hash(dto.getPassword());

    Member member = Member.createMember(dto.getEmail(), hashed, dto.getName());
    memberRepository.save(member);

    return new MemberInfoResponse(member);
  }

  @Transactional
  public MemberInfoResponse signUpWithOIDC(OIDCProvider oidcProvider, String oidcId,
                                           String name,
                                           String email) {
    Member member = Member.createMemberWithOIDC(oidcProvider, oidcId, email, name);
    memberRepository.save(member);

    return new MemberInfoResponse(member);
  }

  @Transactional
  public RelationshipOperationResponse follow(Long followerId, Long followingId) {
    Member follower = memberRepository.findById(followerId);
    Member following = memberRepository.findById(followingId);

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

    follower.unfollow(following);

    return new RelationshipOperationResponse(followerId, followingId);
  }

  @Transactional
  public void addAchievement(Long memberId, Long achvId) {
    Achievement achievement = achvRepository.findAchievementById(achvId);
    Member member = memberRepository.findById(memberId);
    member.addAchievement(achievement);
  }

  @Transactional
  public List<ToggleAchievementExposureResponse> toggleAchievementsExposure(Long memberId,
                                                                            List<ToggleAchievementExposureRequest> dtos) {
    Member member = memberRepository.findById(memberId);
    List<MemberAchievement> memAchvs = member.getAchievements();

    Map<Long, Boolean> requestedAchvMap = new HashMap<>();
    for (ToggleAchievementExposureRequest dto : dtos) {
      requestedAchvMap.put(dto.getAchvId(), dto.isExposure());
    }

    List<ToggleAchievementExposureResponse> response = new ArrayList<>();
    memAchvs.forEach(
        memAchv -> {
          Long achvId = memAchv.getAchievement().getId();
          if (requestedAchvMap.containsKey(achvId)) {
            memAchv.toggleExposure(requestedAchvMap.get(achvId));
            response.add(new ToggleAchievementExposureResponse(achvId, memAchv.isExposed()));
          }
        });
    return response;
  }


  /*------------
   @ Queries
   ------------*/


  public MemberProfileResponse getMemberProfileById(Long id) {

    MemberProfileResponse profile = memberQueryRepository.getMemberProfileById(id);
    if (profile == null) throw new NoSuchMemberException();

    return profile;
  }

  public MemberInfoResponse getMemberInfoById(Long id) {
    MemberInfoResponse memberInfo = memberQueryRepository.getMemberInfoById(id);
    if (memberInfo == null) throw new NoSuchMemberException();

    return memberInfo;
  }

  public MemberInfoResponse getMemberInfoByOidcId(OIDCProvider oidcProvider,
                                                  String oidcId) {
    MemberInfoResponse memberInfo = memberQueryRepository.getMemberInfoByOidcId(
        oidcProvider, oidcId);
    if (memberInfo == null) throw new NoSuchMemberException();

    return memberInfo;
  }

  public MemberCredResponse getMemberCredByEmail(String email) {
    MemberCredResponse memberCred = memberQueryRepository.getMemberCredByEmail(email);
    if (memberCred == null) throw new NoSuchMemberException();

    return memberCred;
  }

  public List<MemberProfileResponse> getFollowers(Long memberId) {
    return memberQueryRepository.getFollowersProfiles(memberId);
  }

  public List<MemberProfileResponse> getFollowings(Long memberId) {
    return memberQueryRepository.getFollowingsProfiles(memberId);
  }

  public List<AchievementResponse> getAllMemberAchievements(Long memberId) {
    return memberQueryRepository.getMemberAchievements(memberId, false);
  }

  public List<AchievementResponse> getExposedMemberAchievements(Long memberId) {
    return memberQueryRepository.getMemberAchievements(memberId, true);
  }
}