package com._2cha.demo.global.exception;

public class NoSuchReviewException extends NotFoundException {

  public NoSuchReviewException(Long reviewId) {
    super("No such review with id " + reviewId, "noSuchReview");
  }
}
