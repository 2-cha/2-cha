package com._2cha.demo.review.exception;

import com._2cha.demo.global.exception.NotFoundException;

public class NoSuchReviewException extends NotFoundException {

  public NoSuchReviewException(Long reviewId) {
    super("No such review with id " + reviewId, "noSuchReview");
  }
}
