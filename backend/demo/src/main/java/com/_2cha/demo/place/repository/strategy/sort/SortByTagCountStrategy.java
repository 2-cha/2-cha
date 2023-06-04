package com._2cha.demo.place.repository.strategy.sort;

import static com._2cha.demo.place.domain.QPlace.place;
import static com._2cha.demo.place.dto.SortOrder.ASC;
import static com._2cha.demo.review.domain.QReview.review;
import static com._2cha.demo.review.domain.QTagInReview.tagInReview;

import com._2cha.demo.place.dto.SortOrder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;

public class SortByTagCountStrategy implements
                                    SortStrategy {

  @Override
  public JPAQuery<Tuple> apply(JPAQueryFactory q,
                               NumberExpression<Double> distanceSphere,
                               SortOrder sortOrder,
                               List<?> filterValues) {

    return q.select(place, distanceSphere, tagInReview.tag.count())
            .from(place)
            .leftJoin(review).on(review.place.id.eq(place.id))
            .leftJoin(review.tagsInReview, tagInReview).on(anyTagsIn((List<Long>) filterValues))
            .groupBy(place)
            .orderBy(tagCountWithOrder(sortOrder), distanceSphere.asc());
  }

  private OrderSpecifier<Long> tagCountWithOrder(SortOrder order) {
    return order == ASC ? tagInReview.count().asc()
                        : tagInReview.count().desc();
  }

  private BooleanExpression anyTagsIn(List<Long> filterValues) {
    return tagInReview.tag.id.in(filterValues);
  }
}
