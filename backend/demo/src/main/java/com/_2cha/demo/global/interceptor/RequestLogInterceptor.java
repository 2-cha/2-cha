package com._2cha.demo.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.MapMessage;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j(topic = "RequestLogger")
public class RequestLogInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    log.info("[{}][{}] {}", request.getMethod(), request.getRequestURI(),
             new MapMessage<>(request.getParameterMap()));

    return true;
  }
}
