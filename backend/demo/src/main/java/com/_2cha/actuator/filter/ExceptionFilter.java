package com._2cha.actuator.filter;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com._2cha.demo.global.advice.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ExceptionFilter implements Filter {

  private final ObjectMapper objectMapper;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    httpResponse.setContentType(APPLICATION_JSON_VALUE);
    try {
      chain.doFilter(request, response);
    } catch (Exception e) {
      ApiError apiError = new ApiError(e);
      httpResponse.getWriter().write(objectMapper.writeValueAsString(apiError));
    }
  }
}
