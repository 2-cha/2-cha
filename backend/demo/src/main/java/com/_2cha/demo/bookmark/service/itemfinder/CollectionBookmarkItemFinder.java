package com._2cha.demo.bookmark.service.itemfinder;


import com._2cha.demo.bookmark.domain.ItemType;
import com._2cha.demo.collection.domain.Collection;
import com._2cha.demo.collection.repository.CollectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CollectionBookmarkItemFinder implements
                                          BookmarkItemFinder {

  private ItemType itemType = ItemType.COLLECTION;
  private final CollectionRepository repository;

  @Override
  public Object findItemById(Long itemId) {
    Collection collection = repository.findCollectionById(itemId);
    if (collection == null || collection.isExposed() == false) return null;
    return collection;
  }

  @Override
  public ItemType getItemType() {
    return itemType;
  }
}
