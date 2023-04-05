package com._2cha.demo.member.domain;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Getter
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Achievement {

  @Id
  @GeneratedValue
  @Column(name = "ACHV_ID")
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private String badgeUrl;

  public static Achievement createMockAchievement() {

    Achievement achievement = new Achievement();
    achievement.name = "Rising Star";
    achievement.description = "You got first follower";
    achievement.badgeUrl = "https://picsum.photos/64/64";
    return achievement;
  }
}
