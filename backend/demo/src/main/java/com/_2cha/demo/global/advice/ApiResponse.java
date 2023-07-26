package com._2cha.demo.global.advice;

import lombok.Data;
import org.springframework.http.HttpStatusCode;

@Data
public class ApiResponse<T> {

  private boolean success = true;
  private HttpStatusCode status;
  private T data;

  public ApiResponse(HttpStatusCode status, T data) {
    this.status = status;
    this.data = data;
  }
}
