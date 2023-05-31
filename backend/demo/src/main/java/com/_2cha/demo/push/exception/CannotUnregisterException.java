package com._2cha.demo.push.exception;

import com._2cha.demo.global.exception.ForbiddenException;

public class CannotUnregisterException extends ForbiddenException {

  public CannotUnregisterException() {
    super("Cannot unregister other member's subject.", "cannotUnregister");
  }
}
