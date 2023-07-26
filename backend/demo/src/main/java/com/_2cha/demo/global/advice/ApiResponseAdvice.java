package com._2cha.demo.global.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
@Slf4j
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {


  @Override
  public boolean supports(MethodParameter returnType,
                          Class<? extends HttpMessageConverter<?>> converterType) {

    return MappingJackson2HttpMessageConverter.class.isAssignableFrom(converterType);

    /**
     * class com._2cha.demo.global.advice.ApiResponse cannot be cast to class java.lang.String (com._2cha.demo.global.advice.ApiResponse is in unnamed module of loader org.springframework.boot.devtools.restart.classloader.RestartClassLoader @2ce05c85; java.lang.String is in module java.base of loader 'bootstrap')
     * StringHttpMessageConverter 가 기대하는 String Body를 객체(ApiResponse)로 바꿔서 생긴 현상.
     * 객체를 처리할 수 있는 MappingJackson2HttpMessageConverter 인 경우에만 beforeBodyWrite()를 호출하도록 설정.
     */
  }

  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                MediaType selectedContentType,
                                Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                ServerHttpRequest request, ServerHttpResponse response) {

    // ExceptionHandler has higher priority,
    // so if exception was thrown `ResponseEntity#body` was already wrapped as ApiError
    if (body instanceof ApiError<?>) {
      return body;
    }
    ServletServerHttpResponse servletResponse = (ServletServerHttpResponse) response;
    int status = servletResponse.getServletResponse().getStatus();
    return new ApiResponse<>(HttpStatusCode.valueOf(status), body);
  }
}
