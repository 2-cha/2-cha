package com._2cha.demo.collection.domain;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;
import static org.hibernate.annotations.OnDeleteAction.CASCADE;

import com._2cha.demo.member.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;

@Entity
@Getter
@Table(
    uniqueConstraints =
    @UniqueConstraint(columnNames = {"MEMBER_ID", "COLL_ID"},
        name = "uk_collection_like_member_id_and_coll_id")
)
@NoArgsConstructor(access = PROTECTED)
public class CollectionLike {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "MEMBER_ID", nullable = false)
  @OnDelete(action = CASCADE)
  private Member member;


  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "COLL_ID", nullable = false)
  @OnDelete(action = CASCADE)
  private Collection collection;

  public static CollectionLike createLike(Member member, Collection collection) {
    CollectionLike like = new CollectionLike();

    like.member = member;
    like.collection = collection;

    return like;
  }
}
