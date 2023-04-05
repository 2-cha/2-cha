package com._2cha.demo.review.repository;

import com._2cha.demo.review.domain.Tag;
import java.util.List;
import org.springframework.data.repository.Repository;

public interface TagRepository extends Repository<Tag, Long> {

  Tag save(Tag tag);

  Tag findTagById(Long id);

  List<Tag> findAll();
}
