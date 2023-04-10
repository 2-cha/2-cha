package com._2cha.demo.global.config;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("cors")
@Getter
@Setter
public class CorsConfig {

  private List<String> origins;
}
