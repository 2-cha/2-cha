package com._2cha.demo.place.exception;

import com._2cha.demo.global.exception.BadRequestException;

public class InvalidTagCountSortException extends BadRequestException {

  public InvalidTagCountSortException() {
    super("Sorting by tag count is only allowed with tag filter",
          "invalidTagCountSort");
  }
}
