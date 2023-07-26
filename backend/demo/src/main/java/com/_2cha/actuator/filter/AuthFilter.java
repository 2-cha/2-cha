package com._2cha.actuator.filter;

import static com._2cha.demo.member.domain.Role.ADMIN;

import com._2cha.demo.auth.dto.JwtAccessTokenPayload;
import com._2cha.demo.auth.exception.NoPrivilegeException;
import com._2cha.demo.auth.exception.NoTokenException;
import com._2cha.demo.auth.exception.NotBearerSchemeException;
import com._2cha.demo.auth.service.AuthService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
public class AuthFilter implements Filter {

  private final AuthService authService;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    String authHeader = httpRequest.getHeader("Authorization");
    if (authHeader == null) {
      throw new NoTokenException();
    }
    String[] bearerToken = authHeader.split(" ");
    if (bearerToken.length != 2 || !bearerToken[0].equals("Bearer")) {
      throw new NotBearerSchemeException();
    }

    JwtAccessTokenPayload payload = authService.verifyJwt(bearerToken[1],
                                                          JwtAccessTokenPayload.class);
    if (payload.getRole() != ADMIN) {throw new NoPrivilegeException(ADMIN.toString());}

    chain.doFilter(request, response);
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    Filter.super.init(filterConfig);
  }

  @Override
  public void destroy() {
    Filter.super.destroy();
  }
}
