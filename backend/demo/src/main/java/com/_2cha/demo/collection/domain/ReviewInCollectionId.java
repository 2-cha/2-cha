package com._2cha.demo.collection.domain;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class ReviewInCollectionId implements Serializable {

  private Long collection;
  private Long review;
}
