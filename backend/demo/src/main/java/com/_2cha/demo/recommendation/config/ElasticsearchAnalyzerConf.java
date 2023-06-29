package com._2cha.demo.recommendation.config;

import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurationContext;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurer;

public class ElasticsearchAnalyzerConf implements ElasticsearchAnalysisConfigurer {

  @Override
  public void configure(ElasticsearchAnalysisConfigurationContext context) {
    context.analyzer("nori");
  }
}
