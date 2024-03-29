package com._2cha.demo.collection.domain;


import static org.hibernate.annotations.OnDeleteAction.CASCADE;

import com._2cha.demo.member.domain.Member;
import com._2cha.demo.review.domain.Review;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.springframework.lang.Nullable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewInCollection {

  @Id
  @GeneratedValue
  private Long id;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "COLL_ID")
  private Collection collection;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "REV_ID")
  @OnDelete(action = CASCADE)
  private Review review;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "MEMBER_ID")
  private Member member;

  public static @Nullable ReviewInCollection createReviewInCollection(Member member,
                                                                      Collection collection,
                                                                      Review review) {
    ReviewInCollection r = new ReviewInCollection();
    r.member = member;
    r.collection = collection;
    r.review = review;

    return r;
  }
}
