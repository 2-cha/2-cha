package com._2cha.demo.bookmark.exception;

import com._2cha.demo.global.exception.NotFoundException;

public class NotBookmarkedException extends NotFoundException {

  public NotBookmarkedException() {
    super("Not bookmarked item.", "notBookmarked");
  }
}
