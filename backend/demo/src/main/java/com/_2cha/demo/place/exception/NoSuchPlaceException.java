package com._2cha.demo.place.exception;

import com._2cha.demo.global.exception.NotFoundException;

public class NoSuchPlaceException extends NotFoundException {

  public NoSuchPlaceException() {
    super("No place with such id.", "noSuchPlace");
  }
}
