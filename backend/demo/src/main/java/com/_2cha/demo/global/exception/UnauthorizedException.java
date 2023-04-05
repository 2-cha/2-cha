package com._2cha.demo.global.exception;

import org.springframework.http.HttpStatus;


public class UnauthorizedException extends HttpException {

  public UnauthorizedException(String message) {
    super(message, HttpStatus.UNAUTHORIZED, "unauthorized");
  }

  public UnauthorizedException(String message, String code) {
    super(message, HttpStatus.UNAUTHORIZED, code);
  }
}
