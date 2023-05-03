package com._2cha.demo.bookmark.exception;

import com._2cha.demo.global.exception.NotFoundException;

public class NoSuchItemException extends NotFoundException {

  public NoSuchItemException() {
    super("No item (review, collection, place) with such id.", "noSuchItem");
  }
}
