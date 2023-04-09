package com._2cha.demo.place.repository.strategy.sort;

import static com._2cha.demo.place.domain.QPlace.place;
import static com._2cha.demo.review.domain.QReview.review;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class SortByTagCountStrategy implements
                                    SortStrategy {

  @Override
  public JPAQuery<Tuple> apply(JPAQueryFactory q,
                               NumberExpression<Double> distanceSphere) {

    return q.select(place, distanceSphere, review.tags.size().count())
            .from(place).leftJoin(review).on(review.place.id.eq(place.id))
            .groupBy(place)
            .orderBy(review.tags.size().count().desc(), distanceSphere.asc());
  }
}
