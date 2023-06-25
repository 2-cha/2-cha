package com._2cha.demo.recommendation.repository;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;
import static org.hibernate.annotations.OnDeleteAction.CASCADE;

import com._2cha.demo.collection.domain.Collection;
import com._2cha.demo.member.domain.Member;
import com._2cha.demo.recommendation.Interaction;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class MemberCollectionPreference {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "MEMBER_ID")
  @OnDelete(action = CASCADE)
  private Member member;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "COLL_ID")
  @OnDelete(action = CASCADE)
  private Collection collection;

  private float preference;

  private LocalDateTime created = LocalDateTime.now();

  public MemberCollectionPreference(Member member, Collection collection, Interaction interaction) {
    this.member = member;
    this.collection = collection;
    this.preference = interaction.value;
  }
}
