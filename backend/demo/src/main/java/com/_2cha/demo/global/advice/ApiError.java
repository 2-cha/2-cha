package com._2cha.demo.global.advice;

import com._2cha.demo.global.exception.HttpException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
public class ApiError<Ex extends Exception> {

  private boolean success = false;
  private HttpStatusCode status = HttpStatus.INTERNAL_SERVER_ERROR;
  private String code = "internalServerError";
  private Object message;
  private LocalDateTime timestamp = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

  public ApiError(Ex e) {
    if (e instanceof HttpException httpException) {
      this.code = httpException.getCode();
      this.status = httpException.getStatus();
    }
    this.message = e.getMessage();
  }
}
