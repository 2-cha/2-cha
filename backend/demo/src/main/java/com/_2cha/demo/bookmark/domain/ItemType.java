package com._2cha.demo.bookmark.domain;

public enum ItemType {
  REVIEW(Values.REVIEW), COLLECTION(Values.COLLECTION), PLACE(Values.PLACE);

  public final String value;

  ItemType(String value) {
    this.value = value;
  }

  public static ItemType fromValue(String value) {
    return switch (value) {
      case Values.REVIEW -> REVIEW;
      case Values.COLLECTION -> COLLECTION;
      case Values.PLACE -> PLACE;
      default -> null;
    };
  }

  public static class Values {

    public static final String REVIEW = "R";
    public static final String COLLECTION = "C";
    public static final String PLACE = "P";
  }
}
