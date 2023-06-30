package com._2cha.demo.recommendation.domain;

public enum Interaction {
  VIEW(1), LIKE(3), SHARE(4), BOOKMARK(5);

  public final float value;

  Interaction(float value) {
    this.value = value;
  }
}
