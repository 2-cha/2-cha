package com._2cha.demo.collection.dto;

import com._2cha.demo.bookmark.dto.BookmarkStatus;
import com._2cha.demo.review.dto.LikeStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@Data
public class CollectionBriefResponse {

  private Long id;
  private String title;
  private String description;
  private String thumbnail;

  @JsonInclude(Include.NON_NULL)
  private BookmarkStatus bookmarkStatus;

  @JsonInclude(Include.NON_NULL)
  private LikeStatus likeStatus;

  public CollectionBriefResponse(Long id, String title, String description, String thumbnail) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.thumbnail = thumbnail;
  }
}
