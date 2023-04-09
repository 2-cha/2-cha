package com._2cha.demo.place.repository.strategy.sort;

import static com._2cha.demo.place.domain.QPlace.place;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class SortByDistanceStrategy implements SortStrategy {

  @Override
  public JPAQuery<Tuple> apply(JPAQueryFactory q,
                               NumberExpression<Double> distanceSphere) {
    return q.select(place, distanceSphere)
            .from(place)
            .groupBy(place)
            .orderBy(distanceSphere.asc());
  }
}
