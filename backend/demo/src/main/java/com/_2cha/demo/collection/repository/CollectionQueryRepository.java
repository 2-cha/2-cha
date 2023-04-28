package com._2cha.demo.collection.repository;

import static com._2cha.demo.collection.domain.QCollection.collection;
import static com._2cha.demo.collection.domain.QReviewInCollection.reviewInCollection;
import static com.querydsl.core.types.Projections.constructor;

import com._2cha.demo.collection.domain.Collection;
import com._2cha.demo.collection.domain.ReviewInCollection;
import com._2cha.demo.collection.dto.CollectionReviewsResponse;
import com._2cha.demo.collection.dto.CollectionViewResponse;
import com._2cha.demo.place.dto.PlaceBriefResponse;
import com._2cha.demo.review.domain.Review;
import com._2cha.demo.review.dto.MemberReviewResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
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

  public List<CollectionViewResponse> getMemberCollections(Long memberId, boolean exposureCond) {
    return queryFactory.select(constructor(CollectionViewResponse.class,
                                           collection.id,
                                           collection.title,
                                           collection.description,
                                           collection.thumbnail
                                          ))
                       .from(collection)
                       .where(collection.member.id.eq(memberId), isExposed(exposureCond))
                       .fetch();
  }


  //TODO
  public CollectionReviewsResponse getCollectionDetail(Long collId) {
    Collection coll = queryFactory.selectDistinct(collection)
                                  .from(collection)
                                  .join(collection.reviews, reviewInCollection).fetchJoin()
                                  .join(reviewInCollection.review).fetchJoin()
                                  .where(collection.id.eq(collId))
                                  .fetchOne();

    List<MemberReviewResponse> reviewResponses = new ArrayList<>();
    for (ReviewInCollection revInColl : coll.getReviews()) {
      Review review = revInColl.getReview();
      MemberReviewResponse reviewResponse = new MemberReviewResponse(review);
      reviewResponse.setPlace(new PlaceBriefResponse(review.getPlace()));
      reviewResponses.add(reviewResponse);
    }
    System.out.println("coll = " + coll);
    System.out.println(reviewResponses);

    return new CollectionReviewsResponse(coll, reviewResponses);
  }

  private BooleanExpression isExposed(boolean cond) {
    return cond ? collection.isExposed.eq(true) : null;
  }
}
