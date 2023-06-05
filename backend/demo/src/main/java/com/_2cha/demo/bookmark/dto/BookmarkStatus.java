package com._2cha.demo.bookmark.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class BookmarkStatus {

  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private Boolean isBookmarked;

  private Long count;

  @JsonProperty(value = "is_bookmarked")
  public Boolean isBookmarked() {
    return isBookmarked;
  }

  public void setBookmarked(Boolean bookmarked) {
    isBookmarked = bookmarked;
  }
}
