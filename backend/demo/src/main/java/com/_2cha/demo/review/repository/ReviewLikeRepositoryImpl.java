package com._2cha.demo.review.repository;

import static com._2cha.demo.review.domain.QReviewLike.reviewLike;
import static com.querydsl.core.group.GroupBy.groupBy;

import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ReviewLikeRepositoryImpl {

  private final EntityManager em;
  private final JPAQueryFactory qf;

  public ReviewLikeRepositoryImpl(EntityManager em) {
    this.em = em;
    //issue with transform(): https://github.com/querydsl/querydsl/issues/3428#issuecomment-1337472853
    this.qf = new JPAQueryFactory(JPQLTemplates.DEFAULT, em);
  }

  public Map<Long, Long> countAllGroupByReviewIdIn(List<Long> reviewIds) {
    Map<Long, Long> likeCounts = qf.select(reviewLike.review.id, reviewLike.countDistinct())
                                   .from(reviewLike)
                                   .where(reviewLike.review.id.in(reviewIds))
                                   .groupBy(reviewLike.review.id)
                                   .transform(groupBy(reviewLike.review.id).as(
                                       reviewLike.countDistinct()));
    reviewIds.forEach(id -> likeCounts.putIfAbsent(id, 0L));
    return likeCounts;
  }
}
