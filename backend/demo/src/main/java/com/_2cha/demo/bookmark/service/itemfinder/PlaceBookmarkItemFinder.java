package com._2cha.demo.bookmark.service.itemfinder;

import com._2cha.demo.bookmark.domain.ItemType;
import com._2cha.demo.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlaceBookmarkItemFinder implements
                                     BookmarkItemFinder {

  private ItemType itemType = ItemType.PLACE;
  private final PlaceRepository repository;

  @Override
  public Object findItemById(Long itemId) {
    return repository.findById(itemId);
  }

  @Override
  public ItemType getItemType() {
    return itemType;
  }
}
