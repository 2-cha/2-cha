package com._2cha.demo.place.domain;

import com._2cha.demo.member.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceBookmark {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "MEMBER_ID")
  private Member member;


  @OneToOne
  @JoinColumn(name = "PLACE_ID")
  private Place place;


  public PlaceBookmark(Member member, Place place) {
    this.member = member;
    this.place = place;
  }
}
