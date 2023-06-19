package com._2cha.demo.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("oidc.kakao")
@Getter
@Setter
public class KakaoOIDCConfig {

  private String clientId;
  private String clientSecret;
  private String redirectUri;
  private String scope = "openid profile_nickname account_email profile_image";
  private String responseType = "code";
  private String grantType = "authorization_code";
  private String oauthEndpoint = "https://kauth.kakao.com/oauth/authorize";
  private String tokenEndpoint = "https://kauth.kakao.com/oauth/token";
}
