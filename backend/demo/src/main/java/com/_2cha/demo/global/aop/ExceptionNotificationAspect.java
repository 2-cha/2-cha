package com._2cha.demo.global.aop;

import com._2cha.demo.global.infra.slack.ExceptionNotificationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ExceptionNotificationAspect {

  private final ExceptionNotificationService exNotiService;

  @Pointcut("execution(* com._2cha.demo.global.advice.ApiErrorAdvice.handleExceptionFallback(..))")
  public void unhandledException() {
  }

  @AfterReturning("unhandledException()")
  public void sendNotification(JoinPoint jp) {
    Object[] args = jp.getArgs();
    HttpServletRequest request = (HttpServletRequest) args[0];
    Exception ex = (Exception) args[1];

    exNotiService.send(request.getMethod(), request.getRequestURI(),
                       request.getParameterMap(), ex);
  }
}
