package com._2cha.demo.review.repository;

import static com._2cha.demo.place.domain.QPlace.place;
import static com._2cha.demo.review.domain.QReview.review;
import static com.querydsl.core.group.GroupBy.groupBy;

import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component

public class ReviewRepositoryImpl {

  private final EntityManager em;
  private final JPAQueryFactory queryFactory;

  public ReviewRepositoryImpl(EntityManager em) {
    this.em = em;
    this.queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, em);
  }

  public Map<Long, Long> countByPlaceIdIn(List<Long> placeIds) {
    return queryFactory
        .select(review.place.id, review.count())
        .from(review)
        .where(place.id.in(placeIds))
        .groupBy(review.place.id)
        .transform(groupBy(review.place.id).as(review.count()));
  }
}
