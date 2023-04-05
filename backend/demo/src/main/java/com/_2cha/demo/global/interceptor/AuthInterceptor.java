package com._2cha.demo.global.interceptor;

import com._2cha.demo.auth.dto.JwtTokenPayload;
import com._2cha.demo.auth.service.AuthService;
import com._2cha.demo.global.annotation.Auth;
import com._2cha.demo.global.annotation.Authed;
import com._2cha.demo.global.exception.NoPrivilegeException;
import com._2cha.demo.global.exception.NoTokenException;
import com._2cha.demo.global.exception.NotBearerSchemeException;
import com._2cha.demo.member.domain.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

  private final AuthService authService;

  @Override
  public boolean preHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler) {
    if (!hasAuthAnnotation(handler)) {
      return true;
    }
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null) {
      throw new NoTokenException();
    }
    String[] bearerToken = authHeader.split(" ");
    if (bearerToken.length != 2 || !bearerToken[0].equals("Bearer")) {
      throw new NotBearerSchemeException();
    }

    JwtTokenPayload payload = authService.verifyJwt(bearerToken[1]);
    Role requiredRole = getRole(handler);
    Role memberRole = payload.getRole();

    if (memberRole.ordinal() < requiredRole.ordinal()) {
      throw new NoPrivilegeException(requiredRole.toString());
    }

    if (hasAuthedAnnotationParameter(handler)) {
      request.setAttribute("authedMemberId", payload.getSub());
    }
    return true;
  }

  private boolean hasAuthAnnotation(Object handlerMethod) {
    if (handlerMethod instanceof HandlerMethod) {
      return ((HandlerMethod) handlerMethod).hasMethodAnnotation(Auth.class);
    }
    return false;
  }

  private boolean hasAuthedAnnotationParameter(Object handlerMethod) {
    if (handlerMethod instanceof HandlerMethod) {
      return Arrays.stream(((HandlerMethod) handlerMethod).getMethodParameters())
                   .anyMatch(param -> param.hasParameterAnnotation(Authed.class) &&
                                      Long.class.isAssignableFrom(param.getParameterType())
                            );
    }
    return false;
  }

  private Role getRole(Object handlerMethod) {
    Auth authAnnotation = ((HandlerMethod) handlerMethod).getMethodAnnotation(Auth.class);
    return authAnnotation.value();
  }
}
