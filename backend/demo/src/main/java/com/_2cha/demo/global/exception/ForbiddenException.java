package com._2cha.demo.global.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends HttpException {

  public ForbiddenException(String message) {
    super(message, HttpStatus.FORBIDDEN, "forbidden");
  }

  public ForbiddenException(String message, String code) {
    super(message, HttpStatus.FORBIDDEN, code);
  }
}
