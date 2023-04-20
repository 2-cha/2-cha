package com._2cha.demo.member;

import com._2cha.demo.member.controller.MemberController;
import com._2cha.demo.member.domain.Achievement;
import com._2cha.demo.member.domain.Member;
import com._2cha.demo.member.dto.AchievementResponse;
import com._2cha.demo.member.dto.MemberProfileResponse;
import com._2cha.demo.member.dto.SignUpRequest;
import com._2cha.demo.member.dto.ToggleAchievementExposureRequest;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles({"test"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MemberTests {

  @Autowired
  MemberController memberController;
  @Autowired
  EntityManager em;


  @BeforeEach
  @Transactional
  void mockUp() throws Exception {
    Achievement achievement = Achievement.createAchievement("Rising Star",
                                                            "You got first follower",
                                                            "https://picsum.photos/64/64");
    em.persist(achievement);

    SignUpRequest request1 = new SignUpRequest();
    request1.setName("member1");
    request1.setEmail("member1@2cha.com");
    request1.setPassword("1234");
    memberController.signUp(request1);

    SignUpRequest request2 = new SignUpRequest();
    request2.setName("member2");
    request2.setEmail("member2@2cha.com");
    request2.setPassword("1234");
    memberController.signUp(request2);

    Member member1 = em.find(Member.class, 1L);
    member1.addAchievement(achievement);
    em.persist(member1);
  }

  @Test
  void getMemberProfile() {
    MemberProfileResponse profile = memberController.getMemberProfile(1L);

    Assertions.assertThat(profile)
              .extracting("name")
              .isEqualTo("member1");
  }


  @Test
  void memberAchievementsTest() {
    List<AchievementResponse> allAchvs = memberController.getAllMemberAchievements(1L);
    List<AchievementResponse> exposedAchvs = memberController.getExposedMemberAchievements(1L);

    Assertions.assertThat(allAchvs)
              .extracting("name")
              .containsExactly("Rising Star");

    Assertions.assertThat(exposedAchvs)
              .extracting("name")
              .containsExactly("Rising Star");

    ToggleAchievementExposureRequest request = new ToggleAchievementExposureRequest();
    ArrayList<ToggleAchievementExposureRequest> list = new ArrayList<>();
    request.setAchvId(1L);
    request.setExposure(false);
    list.add(request);

    memberController.toggleAchvsExposure(1L, list);

    exposedAchvs = memberController.getExposedMemberAchievements(1L);
    Assertions.assertThat(exposedAchvs)
              .isEmpty();
  }

  @Test
  void toggleInvalidAchvTest() {
    List<AchievementResponse> allAchvs = memberController.getAllMemberAchievements(1L);
    List<AchievementResponse> exposedAchvs = memberController.getExposedMemberAchievements(1L);
    ToggleAchievementExposureRequest request = new ToggleAchievementExposureRequest();
    ArrayList<ToggleAchievementExposureRequest> list = new ArrayList<>();
    request.setAchvId(99L);
    request.setExposure(false);

    memberController.toggleAchvsExposure(1L, list);
    List<AchievementResponse> newExposedAchvs = memberController.getExposedMemberAchievements(1L);
    Assertions.assertThat(exposedAchvs).isEqualTo(newExposedAchvs);
  }

  @Test
  void relationshipTest() {
    memberController.follow(1L, 2L);
    List<MemberProfileResponse> member1Followings = memberController.getFollowings(1L);
    List<MemberProfileResponse> member2Followers = memberController.getFollowers(2L);

    Assertions.assertThat(member1Followings)
              .extracting("name")
              .containsExactly("member2");
    Assertions.assertThat(member2Followers)
              .extracting("name")
              .containsExactly("member1");

    memberController.unfollow(1L, 2L);
    member1Followings = memberController.getFollowings(1L);
    member2Followers = memberController.getFollowers(2L);
    Assertions.assertThat(member1Followings)
              .isEmpty();
    Assertions.assertThat(member2Followers)
              .isEmpty();
  }
}