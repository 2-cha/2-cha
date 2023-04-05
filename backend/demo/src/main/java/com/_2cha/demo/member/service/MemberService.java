package com._2cha.demo.member.service;

import com._2cha.demo.global.exception.NoSuchMemberException;
import com._2cha.demo.member.domain.Achievement;
import com._2cha.demo.member.domain.Member;
import com._2cha.demo.member.domain.MemberAchievement;
import com._2cha.demo.member.domain.OIDCProvider;
import com._2cha.demo.member.dto.AchievementResponse;
import com._2cha.demo.member.dto.MemberInfoResponse;
import com._2cha.demo.member.dto.MemberInfoWithCredResponse;
import com._2cha.demo.member.dto.MemberProfileResponse;
import com._2cha.demo.member.dto.RelationshipOperationResponse;
import com._2cha.demo.member.dto.SignUpRequest;
import com._2cha.demo.member.dto.ToggleAchievementExposureRequest;
import com._2cha.demo.member.dto.ToggleAchievementExposureResponse;
import com._2cha.demo.member.repository.AchievementRepository;
import com._2cha.demo.member.repository.MemberRepository;
import com._2cha.demo.review.service.ReviewService;
import com._2cha.demo.util.BCryptHashingUtils;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

  private final MemberRepository memberRepository;
  private final AchievementRepository achvRepository;

  private final EntityManager em;
  private ReviewService reviewService;

  @Autowired
  public void setReviewService(ReviewService reviewService) {
    this.reviewService = reviewService;
  }

  /*------------
   @ Commands
   ------------*/
  @Transactional
  public MemberInfoResponse signUp(SignUpRequest dto) {
    String hashed = BCryptHashingUtils.hash(dto.getPassword());

    Member member = Member.createMember(dto.getEmail(), hashed, dto.getName());
    memberRepository.save(member);

    MemberInfoResponse response = new MemberInfoResponse();

    response.setId(member.getId());
    response.setName(member.getName());
    response.setEmail(member.getEmail());
    response.setRole(member.getRole());

    return response;
  }

  @Transactional
  public MemberInfoResponse signUpWithOIDC(OIDCProvider oidcProvider, String oidcId,
                                           String name,
                                           String email) {
    Member member = Member.createMemberWithOIDC(oidcProvider, oidcId, email, name);

    memberRepository.save(member);

    MemberInfoResponse response = new MemberInfoResponse();

    response.setId(member.getId());
    response.setName(member.getName());
    response.setEmail(member.getEmail());
    response.setRole(member.getRole());

    return response;
  }

  @Transactional
  public RelationshipOperationResponse follow(Long followerId, Long followingId) {
    Member follower = memberRepository.findById(followerId);
    Member following = memberRepository.findById(followingId);

    follower.follow(following);

    if (following.getFollowers().size() == 1) {
      addAchievement(followingId, 1L);
    }

    RelationshipOperationResponse response = new RelationshipOperationResponse();
    response.setFollowerId(followerId);
    response.setFollowingId(followingId);
    return response;
  }

  @Transactional
  public RelationshipOperationResponse unfollow(Long followerId, Long followingId) {
    Member follower = memberRepository.findById(followerId);
    Member following = memberRepository.findById(followingId);

    follower.unfollow(following);

    RelationshipOperationResponse response = new RelationshipOperationResponse();
    response.setFollowerId(followerId);
    response.setFollowingId(followingId);
    return response;
  }

  @Transactional
  public void addAchievement(Long memberId, Long achvId) {
    Achievement achievement = achvRepository.findAchievementById(achvId);
    Member member = memberRepository.findById(memberId);
    member.addAchievement(achievement);
  }

  @Transactional
  public List<ToggleAchievementExposureResponse> toggleAchievementsExposure(Long memberId,
                                                                            List<ToggleAchievementExposureRequest> dto) {
    Member member = memberRepository.findById(memberId);
    List<MemberAchievement> memAchvs = member.getAchievements();
    List<ToggleAchievementExposureResponse> response = new ArrayList<>();
    Map<Long, Boolean> requestedAchvMap = new ConcurrentHashMap<>();

    for (ToggleAchievementExposureRequest d : dto) {
      requestedAchvMap.put(d.getAchvId(), d.isExposure());
    }

    memAchvs.forEach(
        memAchv -> {
          Long achvId = memAchv.getAchievement().getId();
          if (requestedAchvMap.containsKey(achvId)) {
            memAchv.toggleExposure(requestedAchvMap.get(achvId));
            ToggleAchievementExposureResponse r = new ToggleAchievementExposureResponse();
            r.setAchvId(achvId);
            r.setExposed(memAchv.isExposed());
            response.add(r);
          }
        });
    return response;
  }


  /*------------
   @ Queries
   ------------*/
  public Member getMemberById(Long id) {
    Member member = memberRepository.findById(id);
    return member;
  }

  public MemberProfileResponse getMemberProfileById(Long id) {
    Member member = memberRepository.findById(id);
    return makeMemberProfileResponse(member);
  }

  public MemberInfoResponse getMemberInfoById(Long id) {
    Member member = memberRepository.findById(id);
    return makeMemberInfoResponse(member);
  }

  public MemberProfileResponse getMemberProfileByEmail(String email) {
    Member member = memberRepository.findByEmail(email);
    return makeMemberProfileResponse(member);
  }

  public MemberInfoResponse getMemberInfoByEmail(String email) {
    Member member = memberRepository.findByEmail(email);
    return makeMemberInfoResponse(member);
  }

  public MemberProfileResponse getOIDCMemberProfile(OIDCProvider oidcProvider, String oidcId) {
    Member member = memberRepository.findByOIDCId(oidcProvider, oidcId);
    return makeMemberProfileResponse(member);
  }

  public MemberInfoResponse getOIDCMemberInfo(OIDCProvider oidcProvider,
                                              String oidcId) {
    Member member = memberRepository.findByOIDCId(oidcProvider, oidcId);
    return makeMemberInfoResponse(member);
  }

  public MemberInfoWithCredResponse getMemberInfoWithCred(String email) {
    Member member = memberRepository.findByEmail(email);
    if (member == null) {
      throw new NoSuchMemberException();
    }
    MemberInfoWithCredResponse response = new MemberInfoWithCredResponse();
    response.setMemberInfoResponse(makeMemberInfoResponse(member));
    response.setPassword(member.getPassword());
    return response;
  }

  public List<MemberProfileResponse> getFollowers(Long memberId) {
    Member member = memberRepository.findById(memberId);
    List<MemberProfileResponse> followers = new ArrayList<>();

    return member.getFollowers()
                 .stream()
                 .map(
                     f -> {
                       MemberProfileResponse followerProfile = new MemberProfileResponse();
                       Member follower = f.getFollower();
                       followerProfile.setId(follower.getId());
                       followerProfile.setName(follower.getName());
                       followerProfile.setProfMsg(follower.getProfMsg());
                       followerProfile.setProfImg(follower.getProfImg());

                       return followerProfile;
                     })
                 .toList();
  }

  public List<MemberProfileResponse> getFollowings(Long memberId) {
    Member member = memberRepository.findById(memberId);
    List<MemberProfileResponse> followings = new ArrayList<>();

    return member.getFollowings()
                 .stream()
                 .map(
                     f -> {
                       MemberProfileResponse followingProfile = new MemberProfileResponse();
                       Member following = f.getFollowing();
                       followingProfile.setId(following.getId());
                       followingProfile.setName(following.getName());
                       followingProfile.setProfMsg(following.getProfMsg());
                       followingProfile.setProfImg(following.getProfImg());

                       return followingProfile;
                     })
                 .toList();
  }

  public List<AchievementResponse> getAllMemberAchievements(Long memberId) {
    Member member = memberRepository.findById(memberId);
    List<MemberAchievement> achievements = member.getAchievements();
    List<AchievementResponse> dtos = new ArrayList<>();

    for (MemberAchievement memAchv : achievements) {
      AchievementResponse dto = new AchievementResponse();
      Achievement achv = memAchv.getAchievement();
      dto.setName(achv.getName());
      dto.setBadgeUrl(achv.getBadgeUrl());
      dtos.add(dto);
    }
    return dtos;
  }

  public List<AchievementResponse> getExposedMemberAchievements(Long memberId) {
    Member member = memberRepository.findById(memberId);
    return member.getAchievements().stream()
                 .filter(memberAchievement -> memberAchievement.isExposed() == true)
                 .map(memberAchievement -> {
                   AchievementResponse achvDto = new AchievementResponse();
                   Achievement achv = memberAchievement.getAchievement();
                   achvDto.setBadgeUrl(achv.getBadgeUrl());
                   achvDto.setName(achv.getName());
                   return achvDto;
                 })
                 .toList();
  }


  /*------------
   @ Etc
   ------------*/
  private MemberProfileResponse makeMemberProfileResponse(Member member) {
    if (member == null) {
      throw new NoSuchMemberException();
    }
    MemberProfileResponse dto = new MemberProfileResponse();

    dto.setId(member.getId());
    dto.setName(member.getName());
    dto.setProfImg(member.getProfImg());
    dto.setProfMsg(member.getProfMsg());
    return dto;
  }

  private MemberInfoResponse makeMemberInfoResponse(Member member) {
    if (member == null) {
      throw new NoSuchMemberException();
    }
    MemberInfoResponse dto = new MemberInfoResponse();

    dto.setId(member.getId());
    dto.setEmail(member.getEmail());
    dto.setName(member.getName());
    dto.setRole(member.getRole());

    return dto;
  }
}