package com._2cha.demo.push.exception;

import com._2cha.demo.global.exception.BadRequestException;

public class InvalidSubjectException extends BadRequestException {

  public InvalidSubjectException() {
    super("Invalid subject format.", "invalidSubject");
  }
}
