package com._2cha.demo.place.repository.strategy.filter;

import static com._2cha.demo.place.domain.QPlace.place;
import static com._2cha.demo.review.domain.QReview.review;

import com._2cha.demo.place.domain.Place;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TagFilterStrategy implements FilterStrategy {

  @Override
  public JPAQuery<Tuple> apply(JPAQuery<Tuple> q, List<?> filterValues) {

    JPQLQuery<Place> subQuery = JPAExpressions.select(review.place)
                                              .from(review)
                                              .join(review.place)
                                              .join(review.tags)
                                              .where(anyTagsIn((List<Long>) filterValues));

    return q.where(place.in(subQuery));
  }

  private BooleanExpression anyTagsIn(List<Long> filterValues) {
    return review.tags.any().id.in(filterValues);
  }
}
