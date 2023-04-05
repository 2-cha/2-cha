package com._2cha.demo.global.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class HttpException extends RuntimeException {

  private final String code;
  private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

  public HttpException(String message, HttpStatus status) {
    super(message);
    this.status = status;
    this.code = null;
  }

  public HttpException(String message, HttpStatus status, String code) {
    super(message);
    this.status = status;
    this.code = code;
  }
}
