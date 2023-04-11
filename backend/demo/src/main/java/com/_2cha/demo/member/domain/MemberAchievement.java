package com._2cha.demo.member.domain;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAchievement {

  @Id
  @ManyToOne
  @JoinColumn(name = "MEMBER_ID")
  private Member member;

  @Id
  @ManyToOne
  @JoinColumn(name = "ACHV_ID")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Achievement achievement;

  @Column(nullable = false)
  private boolean isExposed = true;

  private LocalDateTime achievedDate = LocalDateTime.now();

  /*-----------
   @ Methods
   ----------*/

  public static MemberAchievement createMemberAchievement(Member member, Achievement achievement) {
    MemberAchievement memberAchievement = new MemberAchievement();
    memberAchievement.member = member;
    memberAchievement.achievement = achievement;
    return memberAchievement;
  }

  public void toggleExposure(boolean value) {
    this.isExposed = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof MemberAchievement that)) {
      return false;
    }
    return member.equals(that.member) && achievement.equals(that.achievement);
  }

  @Override
  public int hashCode() {
    return Objects.hash(member, achievement);
  }
}
