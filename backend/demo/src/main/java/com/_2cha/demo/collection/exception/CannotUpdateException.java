package com._2cha.demo.collection.exception;

import com._2cha.demo.global.exception.ForbiddenException;

public class CannotUpdateException extends ForbiddenException {

  public CannotUpdateException() {
    super("Cannot update other member's collection.", "cannotUpdateCollection");
  }
}
