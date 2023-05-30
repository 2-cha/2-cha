package com._2cha.demo.collection.domain;

import com._2cha.demo.member.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CollectionBookmark {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "MEMBER_ID")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Member member;

  @OneToOne()
  @JoinColumn(name = "COLL_ID")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Collection collection;

  public CollectionBookmark(Member member, Collection collection) {
    this.member = member;
    this.collection = collection;
  }
}
