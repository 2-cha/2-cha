package com._2cha.demo.review.repository;

import com._2cha.demo.review.domain.Tag;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface TagRepository extends Repository<Tag, Long> {

  Tag save(Tag tag);

  Tag findTagById(Long id);

  List<Tag> findAll();

  @Query(value = "SELECT * FROM TAG WHERE MSG ~ ?1", nativeQuery = true)
  List<Tag> findTagsByMsgMatchesRegex(String regex);
}
