package com._2cha.demo.bookmark.exception;

import com._2cha.demo.global.exception.BadRequestException;

public class BookmarkTypeMismatchedException extends BadRequestException {

  public BookmarkTypeMismatchedException() {
    super("Bookmark type mismatched", "bookmarkTypeMismatched");
  }
}
