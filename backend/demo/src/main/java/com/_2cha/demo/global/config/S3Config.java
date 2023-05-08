package com._2cha.demo.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("storage.s3")
@Getter
@Setter
public class S3Config {

  private String bucketName;
  private String bucketBaseUrl;
}
