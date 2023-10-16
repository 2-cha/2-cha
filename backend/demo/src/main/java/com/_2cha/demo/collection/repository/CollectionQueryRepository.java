package com._2cha.demo.collection.repository;

import static com._2cha.demo.collection.domain.QCollection.collection;
import static com._2cha.demo.collection.domain.QReviewInCollection.reviewInCollection;
import static com._2cha.demo.place.domain.QPlace.place;
import static com._2cha.demo.review.domain.QReview.review;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.types.Projections.constructor;

import com._2cha.demo.collection.dto.CollectionBriefResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CollectionQueryRepository {

  private final EntityManager em;
  private final JPAQueryFactory queryFactory;

  public CollectionQueryRepository(EntityManager em) {
    this.em = em;
    this.queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, em);
  }


  public List<CollectionBriefResponse> getMemberCollections(Long memberId, boolean exposureOnly,
                                                            String thumbBaseUrl) {
    return queryFactory.select(constructor(CollectionBriefResponse.class,
                                           collection.id,
                                           collection.title,
                                           collection.description,
                                           collection.thumbnailUrlPath.prepend(thumbBaseUrl)
                                          ))
                       .from(collection)
                       .where(collection.member.id.eq(memberId), isExposed(exposureOnly))
                       .fetch();
  }

  public List<CollectionBriefResponse> getCollectionsByIdIn(List<Long> collIds,
                                                            String thumbBaseUrl) {
    return queryFactory.select(constructor(CollectionBriefResponse.class,
                                           collection.id,
                                           collection.title,
                                           collection.description,
                                           collection.thumbnailUrlPath.prepend(thumbBaseUrl)
                                          ))
                       .from(collection)
                       .where(collection.id.in(collIds), isExposed(true))
                       .fetch();
  }

  public List<CollectionBriefResponse> getLatestCollections(String thumbBaseUrl,
                                                            Long offset, Integer limit) {
    return queryFactory.select(constructor(CollectionBriefResponse.class,
                                           collection.id,
                                           collection.title,
                                           collection.description,
                                           collection.thumbnailUrlPath.prepend(thumbBaseUrl)
                                          ))
                       .from(collection)
                       .where(collection.isExposed.eq(true))
                       .orderBy(collection.id.desc())
                       .offset(offset)
                       .limit(limit)
                       .fetch();
  }

  // get place count of each collection, that is within `distance` m from the given location
  public Map<Long, Long> getNearbyPlaceCount(Point<G2D> location, Double distance) {

    BooleanTemplate dwithin = Expressions.booleanTemplate("ST_DWithin({0}, geography({1}), {2})",
                                                          place.location,
                                                          location,
                                                          distance);

    return queryFactory.select(reviewInCollection.collection.id, place.id.count())
                       .from(reviewInCollection)
                       .join(reviewInCollection.review, review)
                       .join(review.place, place)
                       .where(collection.isExposed.eq(true),
                              dwithin.isTrue())
                       .groupBy(reviewInCollection.collection.id)
                       .transform(groupBy(reviewInCollection.collection.id).as(place.id.count()));
  }

  // get place count of each collection
  public Map<Long, Long> getPlaceCount(List<Long> collIds) {
    return queryFactory.select(reviewInCollection, place.id.count())
                       .from(reviewInCollection)
                       .join(reviewInCollection.review, review)
                       .join(review.place, place)
                       .where(collection.isExposed.eq(true),
                              collection.id.in(collIds))
                       .groupBy(reviewInCollection.collection.id)
                       .transform(groupBy(reviewInCollection.collection.id).as(place.id.count()));
  }

  private BooleanExpression isExposed(boolean exposureOnly) {
    return exposureOnly ? collection.isExposed.eq(true) : null;
  }
}
