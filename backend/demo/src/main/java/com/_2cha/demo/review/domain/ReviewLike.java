package com._2cha.demo.review.domain;

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
    @UniqueConstraint(columnNames = {"MEMBER_ID", "REV_ID"},
        name = "uk_review_like_member_id_and_rev_id")
)
@NoArgsConstructor(access = PROTECTED)
public class ReviewLike {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "MEMBER_ID", nullable = false)
  @OnDelete(action = CASCADE)
  private Member member;


  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "REV_ID", nullable = false)
  @OnDelete(action = CASCADE)
  private Review review;

  public static ReviewLike createLike(Member member, Review review) {
    ReviewLike like = new ReviewLike();

    like.member = member;
    like.review = review;

    return like;
  }
}
