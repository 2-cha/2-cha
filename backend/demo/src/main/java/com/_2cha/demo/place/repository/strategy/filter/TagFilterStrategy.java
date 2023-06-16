package com._2cha.demo.place.repository.strategy.filter;

import static com._2cha.demo.place.domain.QPlace.place;
import static com._2cha.demo.review.domain.QReview.review;
import static com._2cha.demo.review.domain.QTagInReview.tagInReview;
import static java.lang.Long.valueOf;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TagFilterStrategy implements FilterStrategy {

  @Override
  public JPAQuery<Tuple> apply(JPAQuery<Tuple> q, List<?> filterValues) {

    JPQLQuery<Long> subQuery = JPAExpressions.select(review.place.id)
                                             .from(review)
                                             .join(tagInReview)
                                             .on(tagInReview.review.id.eq(review.id))
                                             .where(
                                                 tagInReview.tag.id.in((List<Long>) filterValues)
                                                   )
                                             .groupBy(review.place.id)
                                             .having(
                                                 tagInReview.tag.id.countDistinct()
                                                                   .eq(valueOf(
                                                                       filterValues.size())));

    return q.where(place.id.in(subQuery));
  }
}
