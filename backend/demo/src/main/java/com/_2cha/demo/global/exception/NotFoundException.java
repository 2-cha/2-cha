package com._2cha.demo.global.exception;


import org.springframework.http.HttpStatus;

public class NotFoundException extends HttpException {

  public NotFoundException(String message) {
    super(message, HttpStatus.NOT_FOUND, "notFound");
  }

  public NotFoundException(String message, String code) {
    super(message, HttpStatus.NOT_FOUND, code);
  }
}
