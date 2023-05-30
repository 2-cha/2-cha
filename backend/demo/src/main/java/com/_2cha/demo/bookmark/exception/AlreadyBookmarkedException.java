package com._2cha.demo.bookmark.exception;

import com._2cha.demo.global.exception.ConflictException;

public class AlreadyBookmarkedException extends ConflictException {

  public AlreadyBookmarkedException() {
    super("Already bookmarked item.", "alreadyBookmarked");
  }
}
