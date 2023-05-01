package com._2cha.demo.bookmark.domain;

import com._2cha.demo.bookmark.domain.ItemType.Values;
import com._2cha.demo.review.domain.Review;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue(value = Values.REVIEW)
public class ReviewBookmark extends Bookmark {

  @OneToOne
  private Review item;

  ReviewBookmark(Review item) {
    this.item = item;
    this.thumbnail = item.getImages().get(0).getUrl();
  }
}
