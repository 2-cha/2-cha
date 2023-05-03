package com._2cha.demo.bookmark.exception;

import com._2cha.demo.global.exception.ConflictException;

public class DuplicatedBookmarkItemException extends ConflictException {

  public DuplicatedBookmarkItemException() {
    super("Duplicated bookmarked item.", "dupBookmarkItem");
  }
}
