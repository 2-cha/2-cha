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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PushSubject {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  @JoinColumn(name = "MEMBER_ID")
  @OnDelete(action = OnDeleteAction.CASCADE)
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
