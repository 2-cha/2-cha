package com._2cha.demo.push.exception;

import com._2cha.demo.global.exception.NotFoundException;

public class NoRegisteredSubjectException extends NotFoundException {

  public NoRegisteredSubjectException() {
    super("There are no registered subjects.", "noRegisteredSubject");
  }
}
