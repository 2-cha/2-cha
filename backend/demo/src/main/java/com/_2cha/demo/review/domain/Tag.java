package com._2cha.demo.review.domain;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cacheable
@Getter
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {

  @Id
  @GeneratedValue
  @Column(name = "TAG_ID")
  private Long id;

  @Column(nullable = false, unique = true)
  private String msg;

  @Column(nullable = false)
  private String emoji;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Category category;

  public static Tag createTag(String msg, String emoji, Category category) {
    Tag tag = new Tag();
    tag.msg = msg;
    tag.emoji = emoji;
    tag.category = category;

    return tag;
  }

  public static Tag createTag(TagCreationRequest tagReq) {
    return createTag(tagReq.getAcceptedMessage(), tagReq.getAcceptedEmoji(), tagReq.getCategory());
  }
}
