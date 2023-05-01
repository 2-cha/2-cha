package com._2cha.demo.bookmark.service.itemfinder;

import com._2cha.demo.bookmark.domain.ItemType;
import com._2cha.demo.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewBookmarkItemFinder implements
                                      BookmarkItemFinder {

  private ItemType itemType = ItemType.REVIEW;
  private final ReviewRepository repository;

  @Override
  public Object findItemById(Long itemId) {
    return repository.findReviewById(itemId);
  }

  @Override
  public ItemType getItemType() {
    return itemType;
  }
}
