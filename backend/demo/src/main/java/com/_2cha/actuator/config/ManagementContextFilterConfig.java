package com._2cha.actuator.config;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

import com._2cha.actuator.filter.AuthFilter;
import com._2cha.actuator.filter.ExceptionFilter;
import com._2cha.demo.auth.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.web.ManagementContextConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@ManagementContextConfiguration
public class ManagementContextFilterConfig {

  @Autowired
  AuthService authService;

  @Autowired
  ObjectMapper objectMapper;

  @Bean
  public FilterRegistrationBean<AuthFilter> enableAuthFilter() {
    FilterRegistrationBean<AuthFilter> filterRegistrationBean = new FilterRegistrationBean<>();
    filterRegistrationBean.setFilter(new AuthFilter(authService));
    filterRegistrationBean.addUrlPatterns("/*");
    filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST);
    return filterRegistrationBean;
  }

  @Bean
  public FilterRegistrationBean<ExceptionFilter> enableExceptionFilter() {
    FilterRegistrationBean<ExceptionFilter> filterRegistrationBean = new FilterRegistrationBean<>();
    filterRegistrationBean.setFilter(new ExceptionFilter(objectMapper));
    filterRegistrationBean.addUrlPatterns("/*");
    filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST);
    filterRegistrationBean.setOrder(HIGHEST_PRECEDENCE);
    return filterRegistrationBean;
  }
}
