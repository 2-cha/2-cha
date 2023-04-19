package com._2cha.demo.member.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

  /*-----------
   @ Columns
   ----------*/
  @Id
  @GeneratedValue
  @Column(name = "MEMBER_ID")
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = false, unique = true)
  private String email;

  @Column
  private String password;

  @Enumerated(EnumType.STRING)
  private Role role;

  @Lob
  private String profMsg;
  private String profImg;

  @Enumerated(EnumType.STRING)
  private OIDCProvider oidcProvider;
  private String oidcId;


  @OneToMany(fetch = FetchType.LAZY, mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Relationship> followings = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Relationship> followers = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<MemberAchievement> achievements = new ArrayList<>();

  /*-----------
   @ Methods
   ----------*/

  public static Member createMember(String email, String hashedPassword, String name) {
    Member member = new Member();
    member.email = email;
    member.password = hashedPassword;
    member.name = name;
    member.role = Role.MEMBER;

    return member;
  }

  public static Member createMemberWithOIDC(OIDCProvider oidcProvider, String oidcId,
                                            String email, String name) {
    Member member = new Member();
    member.oidcProvider = oidcProvider;
    member.oidcId = oidcId;
    member.email = email;
    member.name = name;
    member.role = Role.MEMBER;

    return member;
  }

  public void follow(Member member) {
    Relationship relationship = Relationship.createRelationship(this, member);
    this.followings.add(relationship);
    member.followers.add(relationship);
  }

  public void unfollow(Member member) {
    Relationship relationship = Relationship.createRelationship(this, member);

    this.followings.remove(relationship);
    member.followers.remove(relationship);
  }

  public void addAchievement(Achievement achievement) {
    MemberAchievement memberAchievement = MemberAchievement.createMemberAchievement(this,
                                                                                    achievement);
    this.achievements.add(memberAchievement);
  }
}
