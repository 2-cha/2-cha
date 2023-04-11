package com._2cha.demo.place.repository;

import static com._2cha.demo.place.domain.QPlace.place;

import com._2cha.demo.place.domain.Place;
import com._2cha.demo.place.dto.FilterBy;
import com._2cha.demo.place.dto.SortBy;
import com._2cha.demo.place.repository.strategy.FilterSortContext;
import com._2cha.demo.place.repository.strategy.filter.CategoryFilterStrategy;
import com._2cha.demo.place.repository.strategy.filter.FilterStrategy;
import com._2cha.demo.place.repository.strategy.filter.NoFilterStrategy;
import com._2cha.demo.place.repository.strategy.filter.TagFilterStrategy;
import com._2cha.demo.place.repository.strategy.sort.SortByDistanceStrategy;
import com._2cha.demo.place.repository.strategy.sort.SortByReviewCountStrategy;
import com._2cha.demo.place.repository.strategy.sort.SortByTagCountStrategy;
import com._2cha.demo.place.repository.strategy.sort.SortStrategy;
import com._2cha.demo.util.GeomUtils;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Repository;


@Repository
@Slf4j
public class PlaceRepositoryImpl implements PlaceRepository {

  private final EntityManager em;
  private final JPAQueryFactory queryFactory;
  private Map<FilterBy, FilterStrategy> filterStrategyMap = new ConcurrentHashMap<>();
  private Map<SortBy, SortStrategy> sortStrategyMap = new ConcurrentHashMap<>();


  public PlaceRepositoryImpl(EntityManager em) {
    this.em = em;
    this.queryFactory = new JPAQueryFactory(em);

    filterStrategyMap.put(FilterBy.DEFAULT, new NoFilterStrategy());
    filterStrategyMap.put(FilterBy.TAG, new TagFilterStrategy());
    filterStrategyMap.put(FilterBy.CATEGORY, new CategoryFilterStrategy());

    sortStrategyMap.put(SortBy.DISTANCE, new SortByDistanceStrategy());
    sortStrategyMap.put(SortBy.TAG_COUNT, new SortByTagCountStrategy());
    sortStrategyMap.put(SortBy.REVIEW_COUNT, new SortByReviewCountStrategy());
  }

  @Override
  public void save(Place place) {
    em.persist(place);
  }

  @Override
  public Place findById(Long id) {
    return em.find(Place.class, id);
  }

  @Override
  public List<Place> findByIdIn(List<Long> ids) {
    return queryFactory.select(place)
                       .from(place)
                       .where(place.id.in(ids))
                       .fetch();
  }

  @Override
  public Place findByName(String name) {
    return em.createQuery("select p from Place p where name like :name", Place.class)
             .setParameter("name", name)
             .getSingleResult();
  }

  @Override
  public List<Place> findAll() {
    return em.createQuery("select p from Place p", Place.class)
             .getResultList();
  }


  @Override
  public List<Object[]> findAround(Double latitude, Double longitude,
                                   Double minDist, Double maxDist,
                                   Integer pageSize,
                                   SortBy sortBy, FilterBy filterBy,
                                   List<?> filterValues) {

    Point location = GeomUtils.createPoint(latitude, longitude);
    FilterSortContext context = new FilterSortContext(queryFactory,
                                                      filterStrategyMap.get(filterBy),
                                                      sortStrategyMap.get(sortBy));

    List<Object[]> results = new ArrayList<>();
    List<Tuple> tuples = context.execute(location, minDist, maxDist, pageSize, filterValues);
    tuples.forEach(tuple -> {
      results.add(new Object[]{tuple.get(0, Place.class), tuple.get(1, Double.class)});
    });
    return results;
  }
}



