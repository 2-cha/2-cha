package com._2cha.demo.place.repository.strategy.sort;

import static com._2cha.demo.place.domain.QPlace.place;
import static com._2cha.demo.place.dto.SortOrder.ASC;

import com._2cha.demo.place.dto.SortOrder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;

public class SortByDistanceStrategy implements SortStrategy {

  @Override
  public JPAQuery<Tuple> apply(JPAQueryFactory q,
                               NumberExpression<Double> distanceSphere,
                               SortOrder sortOrder,
                               List<?> filterValues) {
    return q.select(place, distanceSphere)
            .from(place)
            .groupBy(place)
            .orderBy(withOrder(distanceSphere, sortOrder));
  }

  private OrderSpecifier<Double> withOrder(NumberExpression<Double> dist, SortOrder order) {
    return order == ASC ? dist.asc() : dist.desc();
  }
}
