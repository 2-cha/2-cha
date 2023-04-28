package com._2cha.demo.bookmark.domain;

import com._2cha.demo.bookmark.domain.ItemType.Values;
import com._2cha.demo.collection.domain.Collection;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue(value = Values.COLLECTION)
public class CollectionBookmark extends Bookmark {

  @OneToOne
  private Collection item;

  CollectionBookmark(Collection item) {
    this.item = item;
    this.thumbnail = item.getThumbnail();
  }
}
