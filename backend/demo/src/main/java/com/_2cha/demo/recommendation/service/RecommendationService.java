package com._2cha.demo.recommendation.service;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

import com._2cha.demo.collection.domain.Collection;
import com._2cha.demo.collection.repository.CollectionRepository;
import com._2cha.demo.member.domain.Member;
import com._2cha.demo.member.service.MemberService;
import com._2cha.demo.recommendation.Interaction;
import com._2cha.demo.recommendation.repository.MemberCollectionPreference;
import com._2cha.demo.recommendation.repository.MemberCollectionPreferenceRepository;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.ko.KoreanAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
public class RecommendationService {

  private final MemberService memberService;
  private final CollectionRepository collectionRepository;
  private final MemberCollectionPreferenceRepository memberCollectionPreferenceRepository;

  /*-----------
   @ Lucene for bm25
   ----------*/
  private final Directory directory = FSDirectory.open(Path.of("index2"));
  private final Similarity similarity = new BM25Similarity(1.2f, 0);
  private final IndexWriterConfig indexWriterConfig = new IndexWriterConfig(new KoreanAnalyzer());
  private final IndexWriter writer;

  /*-----------
   @ Mahout for CF
   ----------*/
  private final DataSource dataSource;
  private final DataModel dataModel;
  private final ItemSimilarity itemSimilarity;
  private final ItemBasedRecommender recommender;


  public RecommendationService(DataSource dataSource, MemberService memberService,
                               CollectionRepository collectionRepository,
                               MemberCollectionPreferenceRepository memberCollectionPreferenceRepository)
      throws IOException, TasteException {

    this.dataSource = dataSource;
    this.memberService = memberService;
    this.collectionRepository = collectionRepository;
    this.memberCollectionPreferenceRepository = memberCollectionPreferenceRepository;
    indexWriterConfig.setSimilarity(similarity);
    writer = new IndexWriter(directory, indexWriterConfig);
    dataModel = new PostgreSQLJDBCDataModel(dataSource,
                                            "member_item_preference",
                                            "member_id",
                                            "coll_id",
                                            "preference",
                                            "created");
    itemSimilarity = new PearsonCorrelationSimilarity(dataModel);
    recommender = new GenericItemBasedRecommender(dataModel, itemSimilarity);
  }

//  @SneakyThrows
//  public void addIndex(Index index) {
//    if (!writer.isOpen()) {
//      throw new RuntimeException("IndexWriter is closed");
//    }
//    List<String> fields = Arrays.stream(index.getClass().getDeclaredFields()).map(f -> f.getName())
//                                .toList();
//
//    Document doc = new Document();
//    for (var field : fields) {
//      doc.add(
//          new Field(field, index.getClass().getDeclaredField(field).get(index).toString(),
//                    TYPE_STORED));
//    }
//
//    writer.addDocument(doc);
//    writer.commit();
//  }

  // Text Based Search
  // Query: User preference + Content Watching
//  public void recommend(Index index)
//      throws IOException, ParseException, NoSuchFieldException, IllegalAccessException {
//    IndexReader reader = DirectoryReader.open(directory);
//    IndexSearcher searcher = new IndexSearcher(reader);
//    searcher.setSimilarity(similarity);
//
//    List<String> fields = Arrays.stream(index.getClass().getDeclaredFields()).map(f -> f.getName())
//                                .toList();
//    // Parse a simple query that searches for "text":
//    for (var field : fields) {
//      QueryParser parser = new QueryParser(field, new StandardAnalyzer());
//      String queryText = index.getClass().getDeclaredField(field).get(index).toString();
//      Query query = parser.parse(queryText);
//      ScoreDoc[] hits = searcher.search(query, 10).scoreDocs;
//      StoredFields storedFields = searcher.storedFields();
//
//      log.info("Search field: {}", field);
//      log.info("Search query: {}", queryText);
//
//      for (int i = 0; i < hits.length; i++) {
//
//        Document hitDoc = storedFields.document(hits[i].doc);
//        log.info("Search result: {} ({})", hitDoc.get(field), hits[i].score);
//      }
//    }
//    reader.close();
//  }


  @Transactional(propagation = REQUIRES_NEW)
  public void addPreference(Long userId, Long itemId, Interaction interaction) {
    Member member = memberService.findById(userId);
    Collection collection = collectionRepository.findCollectionById(itemId);
    if (collection == null) return;

    MemberCollectionPreference preference = new MemberCollectionPreference(member, collection,
                                                                           interaction.value);

    memberCollectionPreferenceRepository.save(preference);
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