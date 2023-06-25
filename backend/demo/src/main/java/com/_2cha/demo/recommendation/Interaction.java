package com._2cha.demo.recommendation;

public enum Interaction {
  VIEW(0.3f), LIKE(1.0f), SHARE(1.2f), BOOKMARK(2.0f);

  public final float value;

  Interaction(float value) {
    this.value = value;
  }
}
