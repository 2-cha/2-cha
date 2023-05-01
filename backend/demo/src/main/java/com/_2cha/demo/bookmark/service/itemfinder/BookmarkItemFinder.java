package com._2cha.demo.bookmark.service.itemfinder;


import com._2cha.demo.bookmark.domain.ItemType;

public interface BookmarkItemFinder {

  Object findItemById(Long itemId);

  ItemType getItemType();
}
