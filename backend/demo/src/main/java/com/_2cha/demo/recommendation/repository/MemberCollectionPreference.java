package com._2cha.demo.recommendation.repository;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import com._2cha.demo.collection.domain.Collection;
import com._2cha.demo.member.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class MemberCollectionPreference {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "MEMBER_ID")
  private Member member;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "COLL_ID")
  private Collection collection;

  private float preference;

  private LocalDateTime created = LocalDateTime.now();

  public MemberCollectionPreference(Member member, Collection collection, float preference) {
    this.member = member;
    this.collection = collection;
    this.preference = preference;
  }
}
