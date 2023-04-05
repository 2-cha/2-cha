package com._2cha.demo.global.config;

import com._2cha.demo.auth.service.AuthService;
import com._2cha.demo.global.argresolver.AuthedMemberIdArgumentResolver;
import com._2cha.demo.global.converter.PathToProviderEnumConverter;
import com._2cha.demo.global.interceptor.AuthInterceptor;
import com._2cha.demo.global.interceptor.LogInterceptor;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

  @Autowired
  AuthService authService;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new LogInterceptor());
    registry.addInterceptor(new AuthInterceptor(authService));
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(new AuthedMemberIdArgumentResolver());
  }

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(new PathToProviderEnumConverter());
  }
}
