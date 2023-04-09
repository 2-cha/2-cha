package com._2cha.demo.place.repository.strategy.filter;

import static com._2cha.demo.place.domain.QPlace.place;

import com._2cha.demo.place.domain.Category;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;

public class CategoryFilterStrategy implements FilterStrategy {

  @Override
  public JPAQuery<Tuple> apply(JPAQuery<Tuple> q, List<?> filterValues) {
    return q.where(place.category.in((List<Category>) filterValues));
  }
}
