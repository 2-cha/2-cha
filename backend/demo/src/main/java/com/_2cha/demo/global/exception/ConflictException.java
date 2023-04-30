package com._2cha.demo.global.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends HttpException {

  public ConflictException(String message) {
    super(message, HttpStatus.CONFLICT, "conflict");
  }

  public ConflictException(String message, String code) {
    super(message, HttpStatus.CONFLICT, code);
  }
}
