package com._2cha.demo.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("oidc.google")
@Getter
@Setter
public class GoogleOIDCConfig {

  private String clientId;
  private String clientSecret;
  private String redirectUri;
  private String scope = "openid email profile";
  private String responseType = "code";
  private String grantType = "authorization_code";
  private String oauthEndpoint = "https://accounts.google.com/o/oauth2/v2/auth";
  private String tokenEndpoint = "https://oauth2.googleapis.com/token";
}
