package com._2cha.demo.global.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends HttpException {

  public BadRequestException(String message) {
    super(message, HttpStatus.BAD_REQUEST, "badRequest");
  }

  public BadRequestException(String message, String code) {
    super(message, HttpStatus.BAD_REQUEST, code);
  }
}
