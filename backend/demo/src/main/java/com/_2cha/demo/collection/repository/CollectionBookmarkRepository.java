package com._2cha.demo.collection.repository;

import com._2cha.demo.bookmark.dto.BookmarkCountProjection;
import com._2cha.demo.collection.domain.CollectionBookmark;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;


public interface CollectionBookmarkRepository extends Repository<CollectionBookmark, Long> {

  CollectionBookmark save(CollectionBookmark bookmark);

  CollectionBookmark findByMemberIdAndCollectionId(Long memberId, Long collId);

  List<CollectionBookmark> findAllByMemberIdAndCollectionIdIn(Long memberId, List<Long> collIds);

  List<CollectionBookmark> findAllByMemberId(Long memberId);

  @Query("SELECT b.collection.id as id, count(b) as count " +
         "FROM CollectionBookmark b " +
         "WHERE b.collection.id in ?1 GROUP BY id")
  List<BookmarkCountProjection> countAllByCollectionIdIn(List<Long> collId);

  Long countAllByCollectionId(Long collId);

  void delete(CollectionBookmark bookmark);
}
