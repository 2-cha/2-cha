package com._2cha.demo.global.advice;

import com._2cha.demo.global.exception.HttpException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
@Slf4j
public class ApiErrorAdvice extends ResponseEntityExceptionHandler {


  /*------------------------
   @ For common MVC errors
   ------------------------*/
  @Override
  protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
                                                           HttpHeaders headers,
                                                           HttpStatusCode statusCode,
                                                           WebRequest request) {
    ApiError apiError = new ApiError(ex);
    apiError.setStatus(statusCode);

    return new ResponseEntity<>(apiError, statusCode);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                HttpHeaders headers,
                                                                HttpStatusCode status,
                                                                WebRequest request) {
    log.error("Here!");
    ApiError apiError = new ApiError<>(ex);
    List<String> messages = ex.getFieldErrors().stream()
                              .map((fieldError) -> fieldError.getField() + ": " +
                                                   fieldError.getRejectedValue() + " (@" +
                                                   fieldError.getCode() + ")"
                                  ).toList();

    apiError.setStatus(status);
    apiError.setCode("argumentNotValid");
    apiError.setMessage(messages);

    return new ResponseEntity<>(apiError, status);
  }


  /*------------------------
   @ Others
   ------------------------*/
  @ExceptionHandler
  public ApiError handleExceptionFallback(Exception e) {
    ApiError apiError = new ApiError(e);

    apiError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    apiError.setCode("unhandledException");
    apiError.setMessage("[" + e.getClass().getName() + "]\n-> " + apiError.getMessage());
    return apiError;
  }

  @ExceptionHandler
  public ResponseEntity<ApiError> handleHttpException(HttpException e) {

    ApiError apiError = new ApiError<>(e);

    return new ResponseEntity<>(apiError, e.getStatus());
  }

  @ExceptionHandler
  public ResponseEntity<ApiError> handleConstraintViolationException(
      ConstraintViolationException e) {

    List<String> messages = e.getConstraintViolations().stream()
                             .map(cv -> cv.getPropertyPath() + ": " +
                                        cv.getInvalidValue() + " (@" +
                                        cv.getConstraintDescriptor().getAnnotation()
                                          .annotationType().getSimpleName() + ")"
                                 ).toList();

    ApiError apiError = new ApiError<>(e);
    apiError.setStatus(HttpStatus.BAD_REQUEST);
    apiError.setMessage(messages);
    apiError.setCode("queryParamNotValid");

    return new ResponseEntity<>(apiError, apiError.getStatus());
  }

  @ExceptionHandler
  public ResponseEntity<ApiError> handleJwtVerificationException(JWTVerificationException e) {

    /**
     * AlgorithmMismatchException
     * IncorrectClaimException
     * InvalidClaimException
     * JWTDecodeException
     * MissingClaimException
     * SignatureVerificationException
     * TokenExpiredException
     */
    ApiError apiError = new ApiError<>(e);

    if (e instanceof TokenExpiredException) {
      apiError.setCode("tokenExpired");
    } else {
      apiError.setCode("invalidJwt");
    }

    apiError.setStatus(HttpStatus.UNAUTHORIZED);
    return new ResponseEntity<>(apiError, apiError.getStatus());
  }
}

