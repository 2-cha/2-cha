package com._2cha.demo.global.filter;

import static com.querydsl.core.util.StringUtils.capitalize;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j(topic = "httpLogger")
@Component
public class HttpLoggingFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    if (request instanceof HttpServletRequest req && response instanceof HttpServletResponse res) {
      UUID uuid = UUID.randomUUID();
      log.info("[{}][{} {}{}] <{}>{}", uuid, req.getMethod(), req.getRequestURI(),
               req.getQueryString() != null ? "?" + req.getQueryString() : "",
               req.getHeader("User-Agent"),
               getRequestBody(req));

      if (log.isDebugEnabled()) {
        log.debug("{}", getHeaderString(req));
      }

      chain.doFilter(request, response);

      log.info("[{}] {}", uuid, HttpStatusCode.valueOf(res.getStatus()));
      if (log.isDebugEnabled()) {
        log.debug("{}", getResponseBody(res));
      }
    } else {
      chain.doFilter(request, response);
    }
  }

  private String getHeaderString(HttpServletRequest request) {
    StringBuilder sb = new StringBuilder("\n");
    request.getHeaderNames().asIterator()
           .forEachRemaining(headerName -> sb.append(capitalize(headerName))
                                             .append(": ")
                                             .append(request.getHeader(headerName))
                                             .append("\n"));
    return sb.toString();
  }

  private String getRequestBody(HttpServletRequest request) {
    if (request.getContentType() != null && request.getContentType()
                                                   .contains("multipart/form-data")) {return "";}

    ContentCachingRequestWrapper wrapper = new ContentCachingRequestWrapper(request);
    return new String(wrapper.getContentAsByteArray());
  }

  private String getResponseBody(final HttpServletResponse response) throws IOException {
    ContentCachingResponseWrapper wrapper = new ContentCachingResponseWrapper(response);

    wrapper.copyBodyToResponse();
    return new String(wrapper.getContentAsByteArray());
  }
}
