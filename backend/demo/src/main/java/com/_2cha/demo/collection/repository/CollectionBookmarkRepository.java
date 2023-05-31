package com._2cha.demo.collection.repository;

import com._2cha.demo.collection.domain.CollectionBookmark;
import java.util.List;
import org.springframework.data.repository.Repository;


public interface CollectionBookmarkRepository extends Repository<CollectionBookmark, Long> {

  CollectionBookmark save(CollectionBookmark bookmark);

  CollectionBookmark findByMemberIdAndCollectionId(Long memberId, Long collId);

  List<CollectionBookmark> findAllByMemberIdAndCollectionIdIn(Long memberId, List<Long> collIds);

  List<CollectionBookmark> findAllByMemberId(Long memberId);

  void delete(CollectionBookmark bookmark);
}
