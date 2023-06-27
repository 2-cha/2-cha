package com._2cha.demo.recommendation.service;

import static org.apache.lucene.document.TextField.TYPE_STORED;
import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

import com._2cha.demo.collection.domain.Collection;
import com._2cha.demo.collection.repository.CollectionRepository;
import com._2cha.demo.member.domain.Member;
import com._2cha.demo.member.service.MemberService;
import com._2cha.demo.recommendation.config.LuceneConfig;
import com._2cha.demo.recommendation.dto.DocumentSource;
import com._2cha.demo.recommendation.event.CollectionInteractionCancelEvent;
import com._2cha.demo.recommendation.event.CollectionInteractionEvent;
import com._2cha.demo.recommendation.repository.MemberCollectionPreference;
import com._2cha.demo.recommendation.repository.MemberCollectionPreferenceRepository;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.ko.KoreanAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.StoredFields;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
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
public class RecommendationService {

  private final MemberService memberService;
  private final CollectionRepository collectionRepository;
  private final MemberCollectionPreferenceRepository memberCollectionPreferenceRepository;

  /*-----------
   @ Lucene for bm25
   ----------*/
  private final LuceneConfig luceneConfig;
  private final Similarity similarity = new BM25Similarity(1.2f, 0);
  private final IndexWriterConfig indexWriterConfig = new IndexWriterConfig(new KoreanAnalyzer());
  private final IndexWriter writer;
  private final Directory directory;

  /*-----------
   @ Mahout for CF
   ----------*/
  private final DataSource dataSource;
  private final DataModel dataModel;
  private final ItemSimilarity itemSimilarity;
  private final ItemBasedRecommender recommender;


  public RecommendationService(DataSource dataSource, MemberService memberService,
                               CollectionRepository collectionRepository,
                               MemberCollectionPreferenceRepository memberCollectionPreferenceRepository,
                               LuceneConfig luceneConfig)
      throws IOException, TasteException {
    // lucene
    this.dataSource = dataSource;
    this.luceneConfig = luceneConfig;
    this.directory = FSDirectory.open(Path.of(this.luceneConfig.getIndexPath()));
    indexWriterConfig.setSimilarity(similarity);
    writer = new IndexWriter(directory, indexWriterConfig);

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
  }

  @SneakyThrows
  public void addDocument(DocumentSource src) {
    if (!writer.isOpen()) {
      throw new RuntimeException("IndexWriter is closed");
    }
    Document doc = new Document();
    src.getFields()
       .forEach((k, v) -> doc.add(new Field(k, v, TYPE_STORED)));

    writer.addDocument(doc);
    writer.commit();
  }

  // Text Based Search
  // Query: User preference + Content Watching
  public void recommend(DocumentSource src) throws IOException, ParseException {
    IndexReader reader = DirectoryReader.open(directory);
    IndexSearcher searcher = new IndexSearcher(reader);
    searcher.setSimilarity(similarity);

    // Parse a simple query that searches for "text":

    for (var field : src.getFields().entrySet()) {
      QueryParser parser = new QueryParser(field.getKey(), new StandardAnalyzer());
      Query query = parser.parse(field.getValue());
      ScoreDoc[] hits = searcher.search(query, 10).scoreDocs;
      StoredFields fieldReader = searcher.storedFields();

      for (var hit : hits) {
        Document hitDoc = fieldReader.document(hit.doc);
        log.info("result: {} ({})", hitDoc.get(field.getKey()), hit.score);
      }
    }
    reader.close();
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


  @PreDestroy
  public void destroy() throws IOException {
    directory.close();
    writer.close();
  }
}