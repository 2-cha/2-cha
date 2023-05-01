package com._2cha.demo.bookmark.dto;

import com._2cha.demo.bookmark.domain.Bookmark;
import lombok.Data;

@Data
public class BookmarkCreatedResponse {

  private Long id;

  public BookmarkCreatedResponse(Bookmark bookmark) {
    this.id = bookmark.getId();
  }
}
