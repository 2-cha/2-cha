package com._2cha.demo.collection.repository;

import com._2cha.demo.collection.domain.Collection;
import java.util.List;
import org.springframework.data.repository.Repository;

public interface CollectionRepository extends Repository<Collection, Long> {

  void save(Collection collection);

  void deleteCollectionById(Long id);

  Collection findCollectionById(Long id);

  List<Collection> findCollectionsByMemberId(Long memberId);
}
