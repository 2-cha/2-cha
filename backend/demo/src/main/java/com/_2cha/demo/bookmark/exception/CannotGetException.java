package com._2cha.demo.bookmark.exception;

import com._2cha.demo.global.exception.ForbiddenException;

public class CannotGetException extends ForbiddenException {

  public CannotGetException() {
    super("Cannot get other member's bookmark.", "cannotGetBookmark");
  }
}
