package com._2cha.demo.review.domain;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cacheable
@Getter
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Tag {

  @Id
  @GeneratedValue
  @Column(name = "TAG_ID")
  private Long id;

  @Column(nullable = false, unique = true)
  private String msg;

  @Column(nullable = false)
  private String emoji;
}
