package com._2cha.demo.place.repository.strategy;

import static com._2cha.demo.place.domain.QPlace.place;

import com._2cha.demo.place.dto.SortOrder;
import com._2cha.demo.place.repository.strategy.filter.FilterStrategy;
import com._2cha.demo.place.repository.strategy.sort.SortStrategy;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;

@Slf4j
public class FilterSortContext {

  private final FilterStrategy filterStrategy;
  private final SortStrategy sortStrategy;
  private final JPAQueryFactory qf;


  public FilterSortContext(JPAQueryFactory qf,
                           FilterStrategy filterStrategy,
                           SortStrategy sortStrategy) {
    this.qf = qf;
    this.filterStrategy = filterStrategy;
    this.sortStrategy = sortStrategy;
  }

  public List<Tuple> execute(Point<G2D> location, Double maxDist,
                             Long offset, Integer pageSize,
                             List<?> filterValues, SortOrder sortOrder) {

    NumberTemplate<Double> distance = Expressions.numberTemplate(Double.class,
                                                                 "ST_Distance({0}, geography({1}))",
                                                                 place.location,
                                                                 location);

    BooleanTemplate dwithin = Expressions.booleanTemplate("ST_DWithin({0}, geography({1}), {2})",
                                                          place.location,
                                                          location,
                                                          maxDist);

    JPAQuery<Tuple> query;
    query = sortStrategy.apply(qf, distance, sortOrder, filterValues);
    query = filterStrategy.apply(query, filterValues); // return empty if filterValues is empty
    query.where(dwithin.eq(true));
    query.offset(offset);
    query.limit(pageSize);
    return query.fetch();
  }
}
