package com._2cha.demo.collection.exception;

import com._2cha.demo.global.exception.BadRequestException;

public class InvalidReviewIdIncludedException extends BadRequestException {

  public InvalidReviewIdIncludedException() {
    super("Invalid review id was included.", "invalidIdIncluded");
  }
}
