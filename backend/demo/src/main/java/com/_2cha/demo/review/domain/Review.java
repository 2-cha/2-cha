package com._2cha.demo.review.domain;

import com._2cha.demo.member.domain.Member;
import com._2cha.demo.place.domain.Place;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

//TODO: Member / Place 삭제 시 동작 논의
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Review {

  @Id
  @GeneratedValue
  @Column(name = "REV_ID")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PLACE_ID", nullable = false)
  private Place place;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "MEMBER_ID", nullable = false)
  private Member member;

  @BatchSize(size = 100)
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "review", orphanRemoval = true, cascade = CascadeType.ALL)
  private List<TagInReview> tagsInReview = new ArrayList<>();

  @BatchSize(size = 100)
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = false)
  private List<ReviewImage> images = new ArrayList<>();

  @CreatedDate
  LocalDateTime created = LocalDateTime.now();

  /*-----------
   @ Methods
   ----------*/
  public static Review createReview(Place place, Member member,
                                    List<Tag> tagList,
                                    List<String> imgUrlPaths,
                                    List<String> thumbUrlPaths
                                   ) {

    Review review = new Review();

    review.place = place;
    review.member = member;
    for (Tag tag : tagList) {
      review.addTag(tag);
    }

    if (imgUrlPaths.size() != thumbUrlPaths.size()) return null;
    for (int i = 0; i < imgUrlPaths.size(); i++) {
      review.addImage(imgUrlPaths.get(i), thumbUrlPaths.get(i));
    }

    return review;
  }

  public void addTag(Tag tag) {
    this.tagsInReview.add(TagInReview.createTagInReview(this, tag));
  }

  public List<Tag> getTags() {
    return tagsInReview.stream().map(TagInReview::getTag).toList();
  }

  public void addImage(String urlPath, String thumbUrlPath) {
    ReviewImage reviewImage = new ReviewImage(this, urlPath, thumbUrlPath);
    this.images.add(reviewImage);
  }
}
