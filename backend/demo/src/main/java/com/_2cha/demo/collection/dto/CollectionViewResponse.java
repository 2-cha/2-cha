package com._2cha.demo.collection.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class CollectionViewResponse {

  private Long id;
  private String title;
  private String description;
  private String thumbnail;


  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private Boolean isBookmarked;

  @JsonInclude(Include.NON_NULL)
  @JsonProperty(value = "is_bookmarked")
  public Boolean isBookmarked() {
    return isBookmarked;
  }

  public void setBookmarked(Boolean bookmarked) {
    isBookmarked = bookmarked;
  }

  public CollectionViewResponse(Long id, String title, String description, String thumbnail) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.thumbnail = thumbnail;
  }
}
