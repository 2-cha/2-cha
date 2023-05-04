package com._2cha.demo.global.interceptor;

import com._2cha.demo.auth.dto.JwtAccessTokenPayload;
import com._2cha.demo.auth.exception.NoPrivilegeException;
import com._2cha.demo.auth.exception.NoTokenException;
import com._2cha.demo.auth.exception.NotBearerSchemeException;
import com._2cha.demo.auth.service.AuthService;
import com._2cha.demo.global.annotation.Auth;
import com._2cha.demo.global.annotation.Authed;
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
    if (!(handler instanceof HandlerMethod)) {
      return true;
    }
    HandlerMethod handlerMethod = (HandlerMethod) handler;
    if (!hasAuthAnnotation(handlerMethod)) {
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

    JwtAccessTokenPayload payload = authService.verifyJwt(bearerToken[1],
                                                          JwtAccessTokenPayload.class);
    Role requiredRole = getRole(handlerMethod);
    Role memberRole = payload.getRole();

    if (memberRole.ordinal() < requiredRole.ordinal()) {
      throw new NoPrivilegeException(requiredRole.toString());
    }

    if (hasAuthedAnnotationParameter(handlerMethod)) {
      request.setAttribute("authedMemberId", payload.getSub());
    }
    return true;
  }

  private boolean hasAuthAnnotation(HandlerMethod handlerMethod) {
    return handlerMethod.getBeanType().getAnnotation(Auth.class) != null ||
           handlerMethod.hasMethodAnnotation(Auth.class);
  }

  private boolean hasAuthedAnnotationParameter(HandlerMethod handlerMethod) {
    return Arrays.stream(handlerMethod.getMethodParameters())
                 .anyMatch(param -> param.hasParameterAnnotation(Authed.class) &&
                                    Long.class.isAssignableFrom(param.getParameterType())
                          );
  }

  private Role getRole(HandlerMethod handlerMethod) {
    Auth classAnnotation = handlerMethod.getBeanType().getAnnotation(Auth.class);
    Auth methodAnnotation = handlerMethod.getMethodAnnotation(Auth.class);

    if (methodAnnotation != null) {
      return methodAnnotation.value();
    }
    return classAnnotation.value();
  }
}
