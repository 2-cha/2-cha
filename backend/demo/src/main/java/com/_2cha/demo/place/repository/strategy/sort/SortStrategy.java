package com._2cha.demo.place.repository.strategy.sort;

import com._2cha.demo.place.dto.SortOrder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public interface SortStrategy {

  JPAQuery<Tuple> apply(JPAQueryFactory q, NumberExpression<Double> distanceSphere,
                        SortOrder sortOrder);
}
