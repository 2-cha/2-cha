package com._2cha.demo.member.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@SpringBootTest(classes = MockApplication.class)
public class MemberTests {

  @Autowired
  EntityManager em;


  @BeforeEach
  void createMockData() {
    Member member1 = Member.createMember("member1@2cha.com", "1234", "member1");
    Member member2 = Member.createMember("member2@2cha.com", "1234", "member2");
    Member member3 = Member.createMember("member3@2cha.com", "1234", "member3");

    Achievement achievement = Achievement.createAchievement("Rising Star",
                                                            "You got first follower",
                                                            "https://picsum.photos/64/64");

    em.persist(member1);
    em.persist(member2);
    em.persist(member3);
    em.persist(achievement);
    em.flush();
    em.clear();
  }

  @Test
  void uniqueNameTest() {
    Member newMember = Member.createMember("new@mail.com", "1234", "member1");
    em.persist(newMember);
    Assertions.assertThrows(PersistenceException.class, () -> em.flush());
  }

  @Test
  void uniqueMailTest() {
    Member newMember = Member.createMember("member1@2cha.com", "1234", "newMember");
    em.persist(newMember);
    Assertions.assertThrows(PersistenceException.class, () -> em.flush());
  }

  @Test
  void followTest() {
    Member member1 = em.find(Member.class, 1L);
    Member member2 = em.find(Member.class, 2L);

    member1.follow(member2);
    em.flush();
    em.clear();

    Member follower = em.find(Member.class, 1L);
    Assertions.assertTrue(follower.getFollowings().get(0).getFollowing().getId() == 2L);
  }

  @Test
  void unfollowTest() {
    Member member1 = em.find(Member.class, 1L);
    Member member2 = em.find(Member.class, 2L);

    member1.follow(member2);
    em.flush();
    em.clear();
    member1.unfollow(member2);

    Member follower = em.find(Member.class, 1L);
    Member following = em.find(Member.class, 2L);
    Assertions.assertTrue(follower.getFollowings().size() == 0);
    Assertions.assertTrue(following.getFollowers().size() == 0);
  }

  @Test
  void memAchvCascadeRemoveTest() {

    Member member = em.find(Member.class, 1L);
    Achievement achv = em.find(Achievement.class, 1L);

    member.addAchievement(achv);
    MemberAchievement ma = em.createQuery(
                                 "select ma " +
                                 "from MemberAchievement ma " +
                                 "where ma.member = :member and ma.achievement = :achv",
                                 MemberAchievement.class)
                             .setParameter("member", member)
                             .setParameter("achv", achv)
                             .getSingleResult();
    Assertions.assertTrue(ma.getMember().getId() == 1L && ma.getAchievement().getId() == 1L);
    em.remove(member);
    em.flush();
    em.clear();

    Assertions.assertThrows(NoResultException.class, () ->
                                em.createQuery(
                                      "select ma " +
                                      "from MemberAchievement ma " +
                                      "where ma.member = :member and ma.achievement = :achv",
                                      MemberAchievement.class)
                                  .setParameter("member", member)
                                  .setParameter("achv", achv)
                                  .getSingleResult()
                           );
  }

  @Test
  void orphanRelationshipTest() {

    Member member1 = em.find(Member.class, 1L);
    Member member2 = em.find(Member.class, 2L);

    member1.follow(member2);
    em.flush();
    Relationship result = em.createQuery(
                                "select r " +
                                "from Relationship r " +
                                "where r.following.id = 2", Relationship.class)
                            .getSingleResult();
    Assertions.assertTrue(result.getFollowing().getId() == 2L);

    member1.getFollowings().remove(
        member1.getFollowings().get(0)
                                  );
    Assertions.assertThrows(NoResultException.class, () ->
        em.createQuery(
              "select r " +
              "from Relationship r " +
              "where r.following.id = 2")
          .getSingleResult());
  }

  @Test
  void relationshipCascadeRemoveTest() {

    Member member1 = em.find(Member.class, 1L);
    Member member2 = em.find(Member.class, 2L);

    member1.follow(member2);
    em.flush();
    Relationship result = em.createQuery(
                                "select r " +
                                "from Relationship r " +
                                "where r.following.id = 2", Relationship.class)
                            .getSingleResult();
    Assertions.assertTrue(result.getFollowing().getId() == 2L);

    em.remove(member1);
    Assertions.assertThrows(NoResultException.class, () ->
        em.createQuery(
              "select r " +
              "from Relationship r " +
              "where r.following.id = 2")
          .getSingleResult());
  }
}


@SpringBootApplication
class MockApplication {

  public static void main(String[] args) {
    SpringApplication.run(MockApplication.class, args);
  }
}
