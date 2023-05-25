package com._2cha.demo.review.domain;

import com._2cha.demo.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
import org.springframework.data.annotation.CreatedDate;
import org.springframework.util.StringUtils;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagCreationRequest {

  @Id
  @GeneratedValue
  @Column(name = "TAG_REQ_ID")
  private Long id;

  @Column(nullable = false)
  private String requestedEmoji;

  @Column(nullable = false)
  private String requestedMessage;

  @Column(nullable = false)
  private String acceptedEmoji;

  @Column(nullable = false)
  private String acceptedMessage;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "REQUESTER_ID")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Member requester;

  @CreatedDate
  private LocalDateTime requestedAt = LocalDateTime.now();


  @Enumerated(EnumType.STRING)
  @Column
  private Category category;

  public static TagCreationRequest makeRequest(Member member, String emoji, String message) {
    TagCreationRequest tagCreationRequest = new TagCreationRequest();
    if (member == null || !StringUtils.hasText(emoji) || !StringUtils.hasText(message)) {
      throw new IllegalArgumentException("Fields of request cannot be null or empty string.");
    }
    tagCreationRequest.requester = member;
    tagCreationRequest.requestedEmoji = tagCreationRequest.acceptedEmoji = emoji;
    tagCreationRequest.requestedMessage = tagCreationRequest.acceptedMessage = message;

    return tagCreationRequest;
  }


  public void setCategory(Category category) {
    this.category = category;
  }

  public void modifyEmoji(String emoji) {
    this.acceptedEmoji = emoji;
  }

  public void modifyMessage(String message) {
    this.acceptedMessage = message;
  }
}
