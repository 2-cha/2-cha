package com._2cha.demo.bookmark.domain;

import com._2cha.demo.bookmark.domain.ItemType.Values;
import com._2cha.demo.place.domain.Place;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue(value = Values.PLACE)
public class PlaceBookmark extends Bookmark {

  @OneToOne
  private Place item;


  PlaceBookmark(Place item) {
    this.item = item;
    this.thumbnailUrlPath = item.getThumbnailUrlPath();
  }
}
