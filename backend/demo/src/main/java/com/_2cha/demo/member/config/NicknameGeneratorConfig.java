package com._2cha.demo.member.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class NicknameGeneratorConfig {

  public static final String PREFIX_PATH = "static/nickname-prefix";
  public static final String MIDDLE_PATH = "static/nickname-middle";
  public static final String SUFFIX_PATH = "static/nickname-suffix";
}
