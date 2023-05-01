package com._2cha.demo.bookmark.service.itemfinder;

import com._2cha.demo.bookmark.domain.ItemType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class BookmarkItemFinderFactory {

  private final List<BookmarkItemFinder> itemFinders;
  private final Map<ItemType, BookmarkItemFinder> itemFinderMap = new HashMap<>();

  public BookmarkItemFinderFactory(List<BookmarkItemFinder> itemFinders) {
    this.itemFinders = itemFinders;
    for (BookmarkItemFinder itemFinder : itemFinders) {
      itemFinderMap.put(itemFinder.getItemType(), itemFinder);
    }
  }

  public BookmarkItemFinder getItemFinder(ItemType itemType) {
    return itemFinderMap.get(itemType);
  }
}
