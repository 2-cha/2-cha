package com._2cha.demo.global.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
@Slf4j
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {


  @Override
  public boolean supports(MethodParameter returnType,
                          Class<? extends HttpMessageConverter<?>> converterType) {

    return ResponseEntity.class.isAssignableFrom(returnType.getParameterType()) &&
           MappingJackson2HttpMessageConverter.class.isAssignableFrom(converterType);

    /**
     * return true 였을때, String 타입 들어온 경우 에러 사항 기록하기.
     * class com._2cha.demo.global.advice.ApiResponse cannot be cast to class java.lang.String (com._2cha.demo.global.advice.ApiResponse is in unnamed module of loader org.springframework.boot.devtools.restart.classloader.RestartClassLoader @2ce05c85; java.lang.String is in module java.base of loader 'bootstrap')
     * 이때는 아마 StringHttpMessageConverter 가 돌아가도록 되어 있는데, 중간에 Body를 바꿔치기해서 고장난듯.
     */
  }

  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                MediaType selectedContentType,
                                Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                ServerHttpRequest request, ServerHttpResponse response) {

    // ExceptionHandler has higher priority
    if (body instanceof ApiError<?>) {
      return body;
    }
    return new ApiResponse<>(body); //TODO: set status
  }
}
