package com._2cha.demo.place.repository.strategy.filter;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;

public interface FilterStrategy {

  JPAQuery<Tuple> apply(JPAQuery<Tuple> q, List<?> filterValues);
}
