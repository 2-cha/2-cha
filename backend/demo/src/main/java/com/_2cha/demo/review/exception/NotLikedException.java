package com._2cha.demo.review.exception;

import com._2cha.demo.global.exception.NotFoundException;

public class NotLikedException extends NotFoundException {

  public NotLikedException() {
    super("Not liked item.", "notLiked");
  }
}
