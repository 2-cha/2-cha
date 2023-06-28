package com._2cha.demo.recommendation.service;

import static java.util.Collections.reverseOrder;
import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

import com._2cha.demo.collection.domain.Collection;
import com._2cha.demo.collection.repository.CollectionRepository;
import com._2cha.demo.member.domain.Member;
import com._2cha.demo.member.service.MemberService;
import com._2cha.demo.recommendation.domain.MemberCollectionPreference;
import com._2cha.demo.recommendation.dto.CollectionCorpusSearchParam;
import com._2cha.demo.recommendation.event.CollectionInteractionCancelEvent;
import com._2cha.demo.recommendation.event.CollectionInteractionEvent;
import com._2cha.demo.recommendation.repository.CollectionCorpusRepository;
import com._2cha.demo.recommendation.repository.MemberCollectionPreferenceRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.PostgreSQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;


@Slf4j
@Service
public class CollectionRecommendationService {

  private final MemberService memberService;
  private final CollectionRepository collectionRepository;
  private final MemberCollectionPreferenceRepository memberCollectionPreferenceRepository;
  private final CollectionCorpusRepository corpusRepository;

  /*-----------
   @ Mahout for CF
   ----------*/
  private final DataModel dataModel;
  private final ItemSimilarity itemSimilarity;
  private final ItemBasedRecommender recommender;


  public CollectionRecommendationService(DataSource dataSource, MemberService memberService,
                                         CollectionRepository collectionRepository,
                                         MemberCollectionPreferenceRepository memberCollectionPreferenceRepository,
                                         CollectionCorpusRepository corpusRepository)
      throws TasteException {
    // mahout
    dataModel = new PostgreSQLJDBCDataModel(dataSource,
                                            "member_collection_preference",
                                            "member_id",
                                            "coll_id",
                                            "preference",
                                            "created");
    itemSimilarity = new PearsonCorrelationSimilarity(dataModel);
    recommender = new GenericItemBasedRecommender(dataModel, itemSimilarity);

    // spring
    this.memberService = memberService;
    this.collectionRepository = collectionRepository;
    this.memberCollectionPreferenceRepository = memberCollectionPreferenceRepository;
    this.corpusRepository = corpusRepository; // hibernate-search (lucene)
  }


  // Text Based Search
  // Query: User preference + Content Watching
  @Transactional
  public List<Collection> recommend(CollectionCorpusSearchParam param, double minScore,
                                    int maxSize) {

    Map<Double, List<Collection>> scoreMap = new TreeMap<>(reverseOrder());
    List<Collection> recommended = new ArrayList<>();
    param.getFields().forEach((field, value) -> {
      if (field.equals("id")) return;
      scoreMap.putAll(corpusRepository.searchWithScore(field, value, maxSize));
    });

    scoreMap.entrySet().stream()
            .filter(e -> e.getKey() >= minScore)
            .forEach(e -> {
              List<Collection> list = e.getValue();

              list.stream()
                  .filter(c -> !recommended.contains(c))
                  .forEach(recommended::add);
            });

    recommended.removeIf(c -> c.getId().equals(param.getId()));
    return recommended.stream()
                      .limit(maxSize)
                      .toList();
  }

  @Async("recommendationTaskExecutor")
  @TransactionalEventListener(value = {CollectionInteractionEvent.class}, phase = AFTER_COMMIT)
  @Transactional
  public void addPreference(CollectionInteractionEvent event) {
    Member member = memberService.findById(event.getMemberId());
    Collection collection = collectionRepository.findCollectionById(event.getCollId());
    if (collection == null) return;

    MemberCollectionPreference preference = new MemberCollectionPreference(member,
                                                                           collection,
                                                                           event.getInteraction());
    memberCollectionPreferenceRepository.save(preference);
  }

  @Async("recommendationTaskExecutor")
  @TransactionalEventListener(value = {
      CollectionInteractionCancelEvent.class}, phase = AFTER_COMMIT)
  @Transactional
  public void removePreference(CollectionInteractionCancelEvent event) {
    MemberCollectionPreference preference = memberCollectionPreferenceRepository.findByMemberIdAndCollectionIdAndPreference(
        event.getMemberId(),
        event.getCollId(), event.getInteraction().value);

    if (preference == null) return;
    memberCollectionPreferenceRepository.delete(preference);
  }


  //Collaborative Filtering (item-item)
  public List<RecommendedItem> recommend(Long itemId, int size) {
    try {
      return recommender.mostSimilarItems(itemId, size);
    } catch (TasteException e) {
      return List.of();
    }
  }

  //Collaborative Filtering (item-item)
  public List<RecommendedItem> recommend(List<Long> itemIds, int size) {
    Set<RecommendedItem> items = new HashSet<>() {};
    itemIds.forEach(id -> {
      try {
        items.addAll(recommender.mostSimilarItems(id, size));
      } catch (TasteException ignored) {}
    });

    return items.stream()
                .sorted((a, b) -> -Float.compare(a.getValue(), b.getValue()))
                .limit(size)
                .toList();
  }


  @Transactional
  public void massIndex() throws InterruptedException {
    corpusRepository.massIndex();
  }
}