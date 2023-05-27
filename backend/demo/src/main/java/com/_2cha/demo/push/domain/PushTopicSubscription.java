package com._2cha.demo.push.domain;

import com._2cha.demo.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"MEMBER_ID", "TOPIC"}))
public class PushTopicSubscription {

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private String topic;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "MEMBER_ID", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Member member;

  public static PushTopicSubscription makeSubscription(Member member, String topic) {
    PushTopicSubscription pts = new PushTopicSubscription();
    pts.topic = topic;
    pts.member = member;

    return pts;
  }
}
