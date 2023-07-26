package com._2cha.actuator.config;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

import com._2cha.actuator.filter.AuthFilter;
import com._2cha.actuator.filter.ExceptionFilter;
import com._2cha.demo.auth.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.web.ManagementContextConfiguration;
import org.springframework.boot.actuate.autoconfigure.web.server.ConditionalOnManagementPort;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

@ManagementContextConfiguration
public class ManagementContextFilterConfig {

  @Autowired
  AuthService authService;

  @Autowired
  ObjectMapper objectMapper;

  /**
   * NOTE: Authorization is unnecessary for cluster-internal usage.
   * <p>
   * <p>
   * Filter should be enabled when:
   * <p>
   * 1. If using same port with API server
   * <p>
   * 2. If using different port but it is exposed to external
   * <p>
   * In case of 1, it is enabled by default. But in case of 2, need to enable it manually by
   */

  @Bean
  @Conditional(OnSamePortOrProperty.class)
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

  static class OnSamePortOrProperty extends AnyNestedCondition {

    OnSamePortOrProperty() {
      super(ConfigurationPhase.REGISTER_BEAN);
    }

    @ConditionalOnManagementPort(ManagementPortType.SAME)
    static class OnSamePort {
    }

    @ConditionalOnProperty(name = "management.authorization.enabled", havingValue = "true")
    static class OnProperty {
    }
  }
}
