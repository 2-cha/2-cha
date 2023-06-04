package com._2cha.demo.place.repository.strategy.sort;

import static com._2cha.demo.place.domain.QPlace.place;
import static com._2cha.demo.place.dto.SortOrder.ASC;
import static com._2cha.demo.review.domain.QReview.review;

import com._2cha.demo.place.dto.SortOrder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class SortByReviewCountStrategy implements
                                       SortStrategy {

  @Override
  public JPAQuery<Tuple> apply(JPAQueryFactory q,
                               NumberExpression<Double> distanceSphere,
                               SortOrder sortOrder) {
    return q.select(place, distanceSphere, review.count())
            .from(place).leftJoin(review).on(review.place.id.eq(place.id))
            .groupBy(place)
            .orderBy(placeCountWithOrder(sortOrder),
                     distanceSphere.asc());
  }

  private OrderSpecifier<Long> placeCountWithOrder(SortOrder order) {
    return order == ASC ? place.count().asc() : place.count().desc();
  }
}
