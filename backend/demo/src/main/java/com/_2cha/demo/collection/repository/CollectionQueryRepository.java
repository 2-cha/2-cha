package com._2cha.demo.collection.repository;

import static com._2cha.demo.collection.domain.QCollection.collection;
import static com.querydsl.core.types.Projections.constructor;

import com._2cha.demo.collection.dto.CollectionViewResponse;
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

  public List<CollectionViewResponse> getMemberCollections(Long memberId, boolean exposureCond) {
    return queryFactory.select(constructor(CollectionViewResponse.class,
                                           collection.id,
                                           collection.title,
                                           collection.description,
                                           collection.thumbnail
                                          ))
                       .from(collection)
                       .where(collection.member.id.eq(memberId), isExposed(exposureCond))
                       .fetch();
  }

  private BooleanExpression isExposed(boolean cond) {
    return cond ? collection.isExposed.eq(true) : null;
  }
}
