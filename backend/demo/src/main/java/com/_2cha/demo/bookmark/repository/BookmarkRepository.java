package com._2cha.demo.bookmark.repository;

import com._2cha.demo.bookmark.domain.Bookmark;
import com._2cha.demo.bookmark.domain.ItemType;
import org.springframework.data.repository.Repository;


public interface BookmarkRepository<T extends Bookmark> extends Repository<T, Long> {

  void save(Bookmark bookmark);

  T findById(Long id);

  Bookmark findMemberBookmarkItem(ItemType itemType, Long itemId, Long memberId);

  void deleteBookmarkById(Long id);
}
