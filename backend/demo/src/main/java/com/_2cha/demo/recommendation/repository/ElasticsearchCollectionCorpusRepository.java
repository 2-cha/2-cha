package com._2cha.demo.recommendation.repository;

import static java.util.Collections.reverseOrder;

import com._2cha.demo.collection.domain.Collection;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.backend.elasticsearch.ElasticsearchExtension;
import org.hibernate.search.engine.search.common.ValueConvert;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Profile("prod")
@Slf4j
public class ElasticsearchCollectionCorpusRepository implements CollectionCorpusRepository {

  private final EntityManager em;

  public Map<Double, List<Collection>> searchWithScore(String field, String corpus, int maxSize) {
    Map<Double, List<Collection>> scoreMap = new TreeMap<>(reverseOrder());
    SearchSession searchSession = Search.session(em);
    var query = searchSession.search(Collection.class)
                             .extension(ElasticsearchExtension.get())
                             .where(f -> f.match()
                                          .field(field)
                                          .matching(corpus, ValueConvert.NO))
                             .toQuery();
    var searchResult = query.fetch(maxSize);

    if (log.isDebugEnabled()) {
      log.debug("Query [{}: {}]", field, corpus);
    }
    for (int i = 0; i < searchResult.hits().size(); i++) {
      Collection collection = searchResult.hits().get(i);
      Double score = query.explain(collection.getId()).get("explanation").getAsJsonObject()
                          .get("value").getAsDouble();

      if (log.isDebugEnabled()) {
        log.debug("Collection<{}> matched with score {}", collection.getId(), score);
        log.debug("--> Explanation: {}",
                  query.explain(collection.getId()).get("explanation").getAsJsonObject());
      }
      scoreMap.putIfAbsent(score, new ArrayList<>());
      scoreMap.get(score).add(collection);
    }

    return scoreMap;
  }

  public void massIndex() throws InterruptedException {
    MassIndexer indexer = Search.session(em).massIndexer(Collection.class);
    indexer.startAndWait();
  }
}

