package com._2cha.demo.place.repository;

import static com._2cha.demo.place.domain.QPlace.place;
import static com.querydsl.core.types.Projections.constructor;

import com._2cha.demo.place.domain.Place;
import com._2cha.demo.place.dto.FilterBy;
import com._2cha.demo.place.dto.NearbyPlaceSearchParams;
import com._2cha.demo.place.dto.PlaceBriefResponse;
import com._2cha.demo.place.dto.PlaceBriefWithDistanceResponse;
import com._2cha.demo.place.dto.PlaceDetailResponse;
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
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;


@Repository
public class PlaceQueryRepository {

  private final EntityManager em;
  private final JPAQueryFactory queryFactory;
  private Map<FilterBy, FilterStrategy> filterStrategyMap = new ConcurrentHashMap<>();
  private Map<SortBy, SortStrategy> sortStrategyMap = new ConcurrentHashMap<>();


  public PlaceQueryRepository(EntityManager em) {
    this.em = em;
    this.queryFactory = new JPAQueryFactory(em);

    filterStrategyMap.put(FilterBy.DEFAULT, new NoFilterStrategy());
    filterStrategyMap.put(FilterBy.TAG, new TagFilterStrategy());
    filterStrategyMap.put(FilterBy.CATEGORY, new CategoryFilterStrategy());

    sortStrategyMap.put(SortBy.DISTANCE, new SortByDistanceStrategy());
    sortStrategyMap.put(SortBy.TAG_COUNT, new SortByTagCountStrategy());
    sortStrategyMap.put(SortBy.REVIEW_COUNT, new SortByReviewCountStrategy());
  }


  /**
   * Note: return empty list if filter is applied but filterValues is empty
   */
  public List<Pair<Place, Double>> findAround(NearbyPlaceSearchParams params) {

    Point location = GeomUtils.createPoint(params.getLat(), params.getLon());
    FilterSortContext context = new FilterSortContext(queryFactory,
                                                      filterStrategyMap.get(params.getFilterBy()),
                                                      sortStrategyMap.get(params.getSortBy()));

    List<Pair<Place, Double>> results = new ArrayList<>();
    List<Tuple> tuples = context.execute(location, params.getMaxDist(), params.getOffset(),
                                         params.getPageSize(), params.getFilterValues(),
                                         params.getSortOrder());
    tuples.forEach(tuple -> {
      results.add(Pair.of(tuple.get(0, Place.class), tuple.get(1, Double.class)));
    });
    return results;
  }


  /**
   * @param id
   * @return PlaceBriefResponse, without tagSummary
   */
  public PlaceBriefResponse getPlaceBriefById(Long id, String imgBaseUrl) {
    return queryFactory.select(constructor(PlaceBriefResponse.class,
                                           place.id,
                                           place.name,
                                           place.category,
                                           place.address,
                                           place.imageUrlPath.prepend(imgBaseUrl)
                                          ))
                       .from(place)
                       .where(place.id.eq(id))
                       .fetchOne();
  }

  /**
   * @param ids
   * @return PlaceBriefResponse, without tagSummary
   */
  public List<PlaceBriefResponse> getPlacesBriefsByIdIn(List<Long> ids, String imgBaseUrl) {
    return queryFactory.select(constructor(PlaceBriefResponse.class,
                                           place.id,
                                           place.name,
                                           place.category,
                                           place.address,
                                           place.imageUrlPath.prepend(imgBaseUrl)
                                          ))
                       .from(place)
                       .where(place.id.in(ids))
                       .fetch();
  }

  /**
   * @param id
   * @return PlaceBriefWithDistanceResponse, without tagSummary
   */
  public PlaceBriefWithDistanceResponse getPlaceBriefWithDistance(Long id, Point<G2D> location,
                                                                  String imgBaseUrl) {
    NumberTemplate<Double> distance = Expressions.numberTemplate(Double.class,
                                                                 "ST_Distance({0}, geography({1}))",
                                                                 place.location,
                                                                 location);
    
    return queryFactory.select(constructor(PlaceBriefWithDistanceResponse.class,
                                           place.id,
                                           place.name,
                                           place.category,
                                           place.address,
                                           place.imageUrlPath.prepend(imgBaseUrl),
                                           distance.as("distance")
                                          ))
                       .from(place)
                       .where(place.id.eq(id))
                       .fetchOne();
  }

  /**
   * @param id
   * @return PlaceDetailResponse, without tags
   */
  public PlaceDetailResponse getPlaceDetailById(Long id, String imgBaseUrl) {
    return queryFactory.select(constructor(PlaceDetailResponse.class,
                                           place.id,
                                           place.name,
                                           place.category,
                                           place.address,
                                           place.lotAddress,
                                           place.imageUrlPath.prepend(imgBaseUrl),
                                           place.site,
                                           place.location
                                          ))
                       .from(place)
                       .where(place.id.eq(id))
                       .fetchOne();
  }
}



