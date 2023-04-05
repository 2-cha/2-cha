package com._2cha.demo.global.exception;

public class NoTokenException extends UnauthorizedException {

  public NoTokenException() {
    super("No token included", "noToken");
  }
}
