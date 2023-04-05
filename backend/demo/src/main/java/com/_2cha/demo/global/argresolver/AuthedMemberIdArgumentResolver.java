package com._2cha.demo.global.argresolver;

import com._2cha.demo.global.annotation.Authed;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthedMemberIdArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(Authed.class)
           &&
           Long.class.isAssignableFrom(parameter.getParameterType());
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
      throws Exception {
    HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
    Object authedMemberId = request.getAttribute("authedMemberId");

    return authedMemberId;
  }
}
