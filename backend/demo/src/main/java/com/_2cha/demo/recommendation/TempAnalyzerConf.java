package com._2cha.demo.recommendation;

import org.apache.lucene.analysis.ko.KoreanAnalyzer;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurationContext;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurer;

public class TempAnalyzerConf implements LuceneAnalysisConfigurer {

  @Override
  public void configure(LuceneAnalysisConfigurationContext context) {
    context.similarity(new BM25Similarity(1.2f, 0));
    context.analyzer("korean").instance(new KoreanAnalyzer());
  }
}