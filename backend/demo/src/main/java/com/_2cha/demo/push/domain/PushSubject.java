package com._2cha.demo.push.domain;

import com._2cha.demo.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PushSubject {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne  //TODO: bidirectional for cascading | add OnDelete Cascading
  @JoinColumn(name = "MEMBER_ID")
  private Member member;

  @Column(nullable = false, unique = true, name = "VAL")
  private String value;

  @Column(nullable = false)
  private LocalDateTime lastActiveTime = LocalDateTime.now();

  public static PushSubject register(Member member, String value) {
    PushSubject pushSubject = new PushSubject();
    pushSubject.member = member;
    pushSubject.value = value;
    return pushSubject;
  }

  public void updateActivity() {
    this.lastActiveTime = LocalDateTime.now();
  }
}
