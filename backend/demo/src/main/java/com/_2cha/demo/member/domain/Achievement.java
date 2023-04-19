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
  
  public static Achievement createAchievement(String name, String description, String badgeUrl) {

    Achievement achievement = new Achievement();
    achievement.name = name;
    achievement.description = description;
    achievement.badgeUrl = badgeUrl;
    return achievement;
  }
}
