package com._2cha.demo.bookmark.repository;

import static com._2cha.demo.bookmark.domain.QCollectionBookmark.collectionBookmark;
import static com._2cha.demo.bookmark.domain.QPlaceBookmark.placeBookmark;
import static com._2cha.demo.bookmark.domain.QReviewBookmark.reviewBookmark;
import static com.querydsl.core.types.Projections.constructor;

import com._2cha.demo.bookmark.dto.BookmarkBriefResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class BookmarkQueryRepository {

  private final EntityManager em;
  private final JPAQueryFactory queryFactory;

  public BookmarkQueryRepository(EntityManager em) {
    this.em = em;
    this.queryFactory = new JPAQueryFactory(em);
  }

  public List<BookmarkBriefResponse> getReviewBookmarksByMemberId(Long memberId) {

    return queryFactory.select(constructor(BookmarkBriefResponse.class,
                                           reviewBookmark.id,
                                           reviewBookmark.thumbnailUrlPath
                                          ))
                       .from(reviewBookmark)
                       .where(reviewBookmark.member.id.eq(memberId))
                       .fetch();
  }

  public List<BookmarkBriefResponse> getCollectionBookmarksByMemberId(Long memberId) {

    return queryFactory.select(constructor(BookmarkBriefResponse.class,
                                           collectionBookmark.id,
                                           collectionBookmark.thumbnailUrlPath
                                          ))
                       .from(collectionBookmark)
                       .where(collectionBookmark.member.id.eq(memberId))
                       .fetch();
  }

  public List<BookmarkBriefResponse> getPlaceBookmarksByMemberId(Long memberId) {

    return queryFactory.select(constructor(BookmarkBriefResponse.class,
                                           placeBookmark.id,
                                           placeBookmark.thumbnailUrlPath
                                          ))
                       .from(placeBookmark)
                       .where(placeBookmark.member.id.eq(memberId))
                       .fetch();
  }
}
