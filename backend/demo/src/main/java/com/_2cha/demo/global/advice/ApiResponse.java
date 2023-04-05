package com._2cha.demo.global.advice;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Data
public class ApiResponse<T> {

  private boolean success = true;
  private HttpStatusCode status = HttpStatus.OK;
  private T data;

  public ApiResponse(T data) {
    this.data = data;
  }
}
