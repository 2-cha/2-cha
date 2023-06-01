package com._2cha.demo.review.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagInReview {

  @Id
  @ManyToOne
  @JoinColumn(name = "TAG_ID")
  private Tag tag;

  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "REV_ID")
  private Review review;

  public static TagInReview createTagInReview(Review review, Tag tag) {
    TagInReview tagInReview = new TagInReview();
    tagInReview.tag = tag;
    tagInReview.review = review;

    return tagInReview;
  }
}
