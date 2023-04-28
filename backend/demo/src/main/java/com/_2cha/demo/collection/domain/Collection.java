package com._2cha.demo.collection.domain;

import com._2cha.demo.member.domain.Member;
import com._2cha.demo.review.domain.Review;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Entity
@Getter
public class Collection {

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String description;

  private boolean isExposed = true;

  @Column(nullable = false)
  private String thumbnail;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "collection", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ReviewInCollection> reviews = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "MEMBER_ID")
  private Member member;

  public static Collection createCollection(Member member, String title,
                                            String description, String thumbnail,
                                            List<Review> reviews) {

    Collection collection = new Collection();
    collection.member = member;
    collection.updateTitle(title);
    collection.updateDescription(description);
    collection.updateThumbnail(thumbnail);
    collection.updateReviews(reviews);
    return collection;
  }

  public void updateReviews(List<Review> reviews) {
    this.reviews.clear();
    this.reviews.addAll(reviews.stream().map(
        review -> ReviewInCollection.createReviewInCollection(this.member, this, review)).toList());
  }

  public void updateTitle(String title) {
    this.title = title;
  }

  public void updateDescription(String description) {
    // allow empty string
    this.description = description;
  }

  public void updateThumbnail(String thumbnail) {
    this.thumbnail = thumbnail;
  }

  public void toggleExposure(Boolean exposure) {
    this.isExposed = exposure;
  }
}
