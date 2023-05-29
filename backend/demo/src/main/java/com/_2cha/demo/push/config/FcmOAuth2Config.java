package com._2cha.demo.push.config;

import com.google.auth.oauth2.ServiceAccountCredentials;
import java.io.IOException;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("google.oauth2.service-account")
@Configuration
@Getter
@Setter
/**
 * https://firebase.google.com/docs/cloud-messaging/migrate-v1?hl=ko#provide-credentials-manually
 */
public class FcmOAuth2Config {

  private String privateKey;
  private String projectId;
  private String clientEmail;
  private List<String> scopes;

  @Bean
  ServiceAccountCredentials serviceAccountCredentials() throws IOException {

    return ServiceAccountCredentials.newBuilder()
                                    .setPrivateKeyString(getPrivateKey())
                                    .setClientEmail(getClientEmail())
                                    .setProjectId(getProjectId())
                                    .setScopes(getScopes())
                                    .build();
  }
}
