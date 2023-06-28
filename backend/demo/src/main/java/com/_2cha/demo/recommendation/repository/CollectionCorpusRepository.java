package com._2cha.demo.recommendation.repository;

import static java.util.Collections.reverseOrder;

import com._2cha.demo.collection.domain.Collection;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.backend.lucene.LuceneExtension;
import org.hibernate.search.engine.search.common.ValueConvert;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CollectionCorpusRepository {

  private final EntityManager em;

  public Map<Double, List<Collection>> searchWithScore(String field, String corpus, int maxSize) {
    Map<Double, List<Collection>> scoreMap = new TreeMap<>(reverseOrder());
    SearchSession searchSession = Search.session(em);
    var query = searchSession.search(Collection.class)
                             .extension(LuceneExtension.get())
                             .where(f -> f.match()
                                          .field(field)
                                          .matching(corpus, ValueConvert.NO))
                             .toQuery();
    var searchResult = query.fetch(maxSize);

    for (int i = 0; i < searchResult.hits().size(); i++) {
      Collection collection = searchResult.hits().get(i);
      Double score = query.explain(collection.getId()).getValue().doubleValue();

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

