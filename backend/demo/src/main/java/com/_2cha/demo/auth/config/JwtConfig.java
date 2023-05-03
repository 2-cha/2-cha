package com._2cha.demo.auth.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("jwt")
@Getter
@Setter
public class JwtConfig {

  private String accessKey;
  private String refreshKey;
  private String issuer;

  private final Integer accessLifetime = 15;
  private final Integer refreshLifetime = 60 * 24 * 7;

  public static Integer ACCESS_LIFETIME;
  public static Integer REFRESH_LIFETIME;

  @PostConstruct
  private void setStatic() {
    ACCESS_LIFETIME = accessLifetime;
    REFRESH_LIFETIME = refreshLifetime;
  }
}

