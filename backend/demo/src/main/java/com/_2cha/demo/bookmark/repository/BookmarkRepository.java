package com._2cha.demo.bookmark.repository;

import com._2cha.demo.bookmark.domain.Bookmark;
import org.springframework.data.repository.Repository;


public interface BookmarkRepository extends Repository<Bookmark, Long> {

  void save(Bookmark bookmark);

  Bookmark findById(Long id);

  Bookmark findAllByMemberId(Long memberId);

  void deleteBookmarkById(Long id);
}
