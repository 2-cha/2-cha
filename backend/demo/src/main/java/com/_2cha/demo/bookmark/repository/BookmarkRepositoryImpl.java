package com._2cha.demo.bookmark.repository;

import static com._2cha.demo.bookmark.domain.QBookmark.bookmark;
import static com._2cha.demo.bookmark.domain.QCollectionBookmark.collectionBookmark;
import static com._2cha.demo.bookmark.domain.QPlaceBookmark.placeBookmark;
import static com._2cha.demo.bookmark.domain.QReviewBookmark.reviewBookmark;

import com._2cha.demo.bookmark.domain.Bookmark;
import com._2cha.demo.bookmark.domain.ItemType;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

@Component
public class BookmarkRepositoryImpl {

  private final EntityManager em;
  private final JPAQueryFactory queryFactory;

  public BookmarkRepositoryImpl(EntityManager em) {
    this.em = em;
    this.queryFactory = new JPAQueryFactory(em);
  }

  public Bookmark findMemberBookmarkItem(ItemType itemType, Long itemId, Long memberId) {

    return queryFactory.select(bookmark)
                       .from(bookmarkType(itemType))
                       .join(bookmark)
                       .where(bookmark.member.id.eq(memberId), itemIdEq(itemType, itemId))
                       .fetchOne();
  }

  private EntityPath<?> bookmarkType(ItemType itemType) {
    return switch (itemType) {
      case REVIEW -> reviewBookmark;
      case COLLECTION -> collectionBookmark;
      case PLACE -> placeBookmark;
    };
  }


  private Predicate itemIdEq(ItemType itemType, Long itemId) {
    return switch (itemType) {
      case REVIEW -> reviewBookmark.item.id.eq(itemId);
      case COLLECTION -> collectionBookmark.item.id.eq(itemId);
      case PLACE -> placeBookmark.item.id.eq(itemId);
    };
  }
}
