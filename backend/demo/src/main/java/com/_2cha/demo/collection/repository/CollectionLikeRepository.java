package com._2cha.demo.collection.repository;

import com._2cha.demo.collection.domain.CollectionLike;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionLikeRepository extends JpaRepository<CollectionLike, Long> {

  List<CollectionLike> findAllByMemberId(Long memberId);

  CollectionLike findByMemberIdAndCollectionId(Long memberId, Long collId);

  List<CollectionLike> findAllByMemberIdAndCollectionIdIn(Long memberId, List<Long> collIds);

  Long countAllByCollectionId(Long collId);

  Map<Long, Long> countAllGroupByCollectionIdIn(List<Long> collIds);
}
