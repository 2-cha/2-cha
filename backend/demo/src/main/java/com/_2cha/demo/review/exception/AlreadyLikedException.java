package com._2cha.demo.review.exception;

import com._2cha.demo.global.exception.ConflictException;

public class AlreadyLikedException extends ConflictException {

  public AlreadyLikedException() {
    super("Already liked item.", "alreadyLiked");
  }
}
