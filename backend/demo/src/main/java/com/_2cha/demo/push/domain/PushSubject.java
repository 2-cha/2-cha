package com._2cha.demo.push.domain;

import com._2cha.demo.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PushSubject {

  @Id
  private Long id;

  @ManyToOne  //TODO: bidirectional for cascading | add OnDelete Cascading
  @JoinColumn(name = "MEMBER_ID")
  private Member member;

  @Column(nullable = false, unique = true)
  private String value;

  public static PushSubject register(Member member, String value) {
    PushSubject pushSubject = new PushSubject();
    pushSubject.member = member;
    pushSubject.value = value;
    return pushSubject;
  }
}
