package com._2cha.demo.member.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "deleted_at = '1970-01-01 00:00:00'")
@Table(uniqueConstraints = {
    @UniqueConstraint(name = "uk_member_email_deleted_at", columnNames = {"email", "deletedAt"}),
    @UniqueConstraint(name = "uk_member_name_deleted_at", columnNames = {"name", "deletedAt"}),
    @UniqueConstraint(
        name = "uk_member_oidc_provider_oidc_id_deleted_at",
        columnNames = {"oidcProvider", "oidcId", "deletedAt"}
    )
})
public class Member {

  /*-----------
   @ Columns
   ----------*/
  @Id
  @GeneratedValue
  @Column(name = "MEMBER_ID")
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String email;

  @Column
  private String password;

  @Enumerated(EnumType.STRING)
  private Role role;

  private String profMsg;

  private String profImgThumbPath;

  @Enumerated(EnumType.STRING)
  private OIDCProvider oidcProvider;
  private String oidcId;

  private LocalDateTime deletedAt = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);

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
                                            String email, String name, String profImgThumbPath) {
    Member member = new Member();
    member.oidcProvider = oidcProvider;
    member.oidcId = oidcId;
    member.email = email;
    member.name = name;
    member.profImgThumbPath = profImgThumbPath;
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

  public void removeAchievement(Achievement achievement) {
    for (MemberAchievement memberAchievement : this.achievements) {
      if (memberAchievement.getAchievement().equals(achievement)) {
        this.achievements.remove(memberAchievement);
        break;
      }
    }
  }

  public void removeAchievement(MemberAchievement achievement) {
    this.achievements.remove(achievement);
  }

  public void updateProfileImage(String profImgThumbPath) {
    this.profImgThumbPath = profImgThumbPath;
  }

  public void updateProfile(String name, String profMsg) {
    this.name = name;
    this.profMsg = profMsg;
  }

  public void softDelete() {
    this.deletedAt = LocalDateTime.now();
  }
}
