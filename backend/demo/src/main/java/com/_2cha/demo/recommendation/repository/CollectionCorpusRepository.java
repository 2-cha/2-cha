package com._2cha.demo.recommendation.repository;

import com._2cha.demo.collection.domain.Collection;
import java.util.List;
import java.util.Map;

public interface CollectionCorpusRepository {

  Map<Double, List<Collection>> searchWithScore(String field, String corpus, int maxSize);

  void massIndex() throws InterruptedException;
}
