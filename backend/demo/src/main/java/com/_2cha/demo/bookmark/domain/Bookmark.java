package com._2cha.demo.bookmark.domain;

import com._2cha.demo.collection.domain.Collection;
import com._2cha.demo.member.domain.Member;
import com._2cha.demo.place.domain.Place;
import com._2cha.demo.review.domain.Review;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "ITEM_TYPE", discriminatorType = DiscriminatorType.STRING)
public abstract class Bookmark {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "MEMBER_ID")
  private Member member;

  @Column(nullable = false)
  protected String thumbnail;


  public Long getId() {
    return id;
  }

  public Member getMember() {
    return member;
  }

  public static Bookmark createBookmark(Member member, Object item) {
    Bookmark bookmark;
    if (item instanceof Place place) {
      bookmark = new PlaceBookmark(place);
    } else if (item instanceof Review review) {
      bookmark = new ReviewBookmark(review);
    } else if (item instanceof Collection collection) {
      bookmark = new CollectionBookmark(collection);
    } else {
      return null;
    }

    bookmark.member = member;

    return bookmark;
  }

  public String getThumbnail() {return this.thumbnail;}

  public abstract Object getItem();
}
