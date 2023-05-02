package com._2cha.demo.auth.exception;

import com._2cha.demo.global.exception.UnauthorizedException;

public class NoTokenException extends UnauthorizedException {

  public NoTokenException() {
    super("No token included", "noToken");
  }
}
