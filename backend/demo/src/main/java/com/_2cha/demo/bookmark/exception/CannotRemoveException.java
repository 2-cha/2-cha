package com._2cha.demo.bookmark.exception;

import com._2cha.demo.global.exception.ForbiddenException;

public class CannotRemoveException extends ForbiddenException {

  public CannotRemoveException() {
    super("Cannot remove other member's bookmark.", "cannotRemoveBookmark");
  }
}
