package com._2cha.demo.global.config;

import org.apache.tika.Tika;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TikaConfig {

  @Bean
  public Tika tika() {
    return new Tika();
  }
}