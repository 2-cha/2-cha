package com._2cha.demo.collection.exception;

import com._2cha.demo.global.exception.ForbiddenException;

public class CannotAccessToPrivateException extends ForbiddenException {

  public CannotAccessToPrivateException() {
    super("Cannot access to private collection.", "cannotAccessToPrivate");
  }
}