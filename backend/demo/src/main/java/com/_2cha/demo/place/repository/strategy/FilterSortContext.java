package com._2cha.demo.place.repository.strategy;

import static com._2cha.demo.place.domain.QPlace.place;

import com._2cha.demo.place.repository.strategy.filter.FilterStrategy;
import com._2cha.demo.place.repository.strategy.sort.SortStrategy;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;

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

  public List<Tuple> execute(Point location, Double minDist, Double maxDist, Integer pageSize,
                             List<?> filterValues) {
    NumberTemplate<Double> distanceSphere = Expressions.numberTemplate(Double.class,
                                                                       "function('ST_DistanceSphere', {0}, {1})",
                                                                       place.location,
                                                                       location);
    JPAQuery<Tuple> query;

    query = sortStrategy.apply(qf, distanceSphere);
    query = filterStrategy.apply(query, filterValues);
    query.where(distanceSphere.gt(minDist),
                distanceSphere.loe(maxDist));
    query.limit(pageSize);
    return query.fetch();
  }
}
