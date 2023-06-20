package com._2cha.demo.collection.repository;

import static com._2cha.demo.collection.domain.QCollection.collection;
import static com.querydsl.core.types.Projections.constructor;

import com._2cha.demo.collection.dto.CollectionBriefResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CollectionQueryRepository {

  private final EntityManager em;
  private final JPAQueryFactory queryFactory;

  public CollectionQueryRepository(EntityManager em) {
    this.em = em;
    this.queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, em);
  }


  public List<CollectionBriefResponse> getMemberCollections(Long memberId, boolean exposureCond,
                                                            String thumbBaseUrl) {
    return queryFactory.select(constructor(CollectionBriefResponse.class,
                                           collection.id,
                                           collection.title,
                                           collection.description,
                                           collection.thumbnailUrlPath.prepend(thumbBaseUrl)
                                          ))
                       .from(collection)
                       .where(collection.member.id.eq(memberId), isExposed(exposureCond))
                       .fetch();
  }

  public List<CollectionBriefResponse> getCollectionsByIdIn(List<Long> collIds,
                                                            String thumbBaseUrl) {
    return queryFactory.select(constructor(CollectionBriefResponse.class,
                                           collection.id,
                                           collection.title,
                                           collection.description,
                                           collection.thumbnailUrlPath.prepend(thumbBaseUrl)
                                          ))
                       .from(collection)
                       .where(collection.member.id.in(collIds), isExposed(true))
                       .fetch();
  }

  private BooleanExpression isExposed(boolean cond) {
    return cond ? collection.isExposed.eq(true) : null;
  }
}
