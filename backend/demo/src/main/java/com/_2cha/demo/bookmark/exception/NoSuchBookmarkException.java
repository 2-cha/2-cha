package com._2cha.demo.bookmark.exception;

import com._2cha.demo.global.exception.NotFoundException;

public class NoSuchBookmarkException extends NotFoundException {

  public NoSuchBookmarkException() {
    super("No bookmark with such id.", "noSuchBookmark");
  }
}
