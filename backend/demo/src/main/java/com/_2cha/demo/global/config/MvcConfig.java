package com._2cha.demo.global.config;

import com._2cha.demo.auth.converter.PathToProviderEnumConverter;
import com._2cha.demo.auth.service.AuthService;
import com._2cha.demo.global.argresolver.AuthedMemberIdArgumentResolver;
import com._2cha.demo.global.interceptor.AuthInterceptor;
import com._2cha.demo.global.interceptor.RequestLogInterceptor;
import com._2cha.demo.place.converter.FilterByRequestParamConverter;
import com._2cha.demo.place.converter.SortByRequestParamConverter;
import com._2cha.demo.place.converter.SortOrderRequestParamConverter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

  @Autowired
  AuthService authService;
  @Autowired
  CorsConfig corsConfig;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new RequestLogInterceptor());
    registry.addInterceptor(new AuthInterceptor(authService));
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(new AuthedMemberIdArgumentResolver());
  }

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(new PathToProviderEnumConverter());
    registry.addConverter(new FilterByRequestParamConverter());
    registry.addConverter(new SortByRequestParamConverter());
    registry.addConverter(new SortOrderRequestParamConverter());
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
            .allowedOrigins(corsConfig.getOrigins().toArray(String[]::new))
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "HEAD");
  }
}
