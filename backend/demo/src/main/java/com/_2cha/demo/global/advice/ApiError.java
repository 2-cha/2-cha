package com._2cha.demo.global.advice;

import com._2cha.demo.global.exception.HttpException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
public class ApiError<Ex extends Exception> {

  private boolean success = false;
  private HttpStatusCode status;
  private String code;
  private Object message;
  private LocalDateTime timestamp = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

  public ApiError(Ex e) {
    if (e instanceof HttpException) {
      this.code = ((HttpException) e).getCode();
      this.status = ((HttpException) e).getStatus();
    }
    this.message = e.getMessage();
  }
}
