package com._2cha.demo.collection.repository;

import static com._2cha.demo.collection.domain.QCollectionLike.collectionLike;
import static com.querydsl.core.group.GroupBy.groupBy;

import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class CollectionLikeRepositoryImpl {

  private final EntityManager em;
  private final JPAQueryFactory qf;

  public CollectionLikeRepositoryImpl(EntityManager em) {
    this.em = em;
    //issue with transform(): https://github.com/querydsl/querydsl/issues/3428#issuecomment-1337472853
    this.qf = new JPAQueryFactory(JPQLTemplates.DEFAULT, em);
  }

  public Map<Long, Long> countAllGroupByCollectionIdIn(List<Long> collIds) {

    Map<Long, Long> likeCounts = qf.select(collectionLike.collection.id,
                                           collectionLike.countDistinct())
                                   .from(collectionLike)
                                   .where(collectionLike.collection.id.in(collIds))
                                   .groupBy(collectionLike.collection.id)
                                   .transform(groupBy(collectionLike.collection.id).as(
                                       collectionLike.countDistinct()));
    collIds.forEach(id -> likeCounts.putIfAbsent(id, 0L));
    return likeCounts;
  }
}
