package com._2cha.demo.global.infra.slack.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties("slack")
public class SlackConfig {

  private String endpoint;
  private String channel;
  private String username;
}