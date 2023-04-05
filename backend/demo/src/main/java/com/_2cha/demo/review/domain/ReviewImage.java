package com._2cha.demo.review.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewImage {

  @Id
  @GeneratedValue
  @Column(name = "REV_IMG_ID")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "REV_ID", nullable = false)
  private Review review;

  @Column(nullable = false)
  private String url;


  ReviewImage(Review review, String url) {
    this.review = review;
    this.url = url;
  }
}
