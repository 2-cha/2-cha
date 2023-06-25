package com._2cha.demo.recommendation.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "storage.lucene")
@Configuration
@Getter
@Setter
public class LuceneConfig {

  String indexPath;
}
