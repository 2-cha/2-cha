package com._2cha.demo.collection.exception;

import com._2cha.demo.global.exception.ForbiddenException;

public class CannotCreateException extends ForbiddenException {

  public CannotCreateException() {
    super("Cannot create collection with other member's review.", "cannotCreateCollection");
  }
}
