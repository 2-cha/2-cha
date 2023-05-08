package com._2cha.demo.review.domain;

import com._2cha.demo.member.domain.Member;
import com._2cha.demo.place.domain.Place;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

//TODO: Member / Place 삭제 시 동작 논의
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(
      name = "TAG_IN_REVIEW",
      joinColumns = @JoinColumn(name = "REV_ID"),
      inverseJoinColumns = @JoinColumn(name = "TAG_ID")
  )
  private List<Tag> tags = new ArrayList<>();

  @BatchSize(size = 100)
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = false)
  private List<ReviewImage> images = new ArrayList<>();

  LocalDateTime created;

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
    this.tags.add(tag);
  }

  public void addImage(String urlPath, String thumbUrlPath) {
    ReviewImage reviewImage = new ReviewImage(this, urlPath, thumbUrlPath);
    this.images.add(reviewImage);
  }
}
