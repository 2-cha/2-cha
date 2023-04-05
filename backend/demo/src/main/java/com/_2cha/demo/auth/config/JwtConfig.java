package com._2cha.demo.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("jwt")
@Getter
@Setter
public class JwtConfig {

  private String key;
  private String issuer;

  private final Integer accessLifetime = 15;
  private final Integer refreshLifetime = 60 * 24 * 7;
}

