package com._2cha.demo.collection.domain;


import com._2cha.demo.member.domain.Member;
import com._2cha.demo.review.domain.Review;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(ReviewInCollectionId.class)
public class ReviewInCollection {

  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "COLL_ID")
  private Collection collection;

  @Id
//  @ManyToOne(fetch = FetchType.LAZY)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "REV_ID")
  private Review review;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "MEMBER_ID")
  private Member member;

  public static @Nullable ReviewInCollection createReviewInCollection(Member member,
                                                                      Collection collection,
                                                                      Review review) {

    //TODO: to service layer (could be MSA)
    if (member != collection.getMember() ||
        member != review.getMember()) {return null;}

    ReviewInCollection r = new ReviewInCollection();
    r.member = member;
    r.collection = collection;
    r.review = review;

    return r;
  }
}
