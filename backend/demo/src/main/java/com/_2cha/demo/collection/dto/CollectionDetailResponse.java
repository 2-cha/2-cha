package com._2cha.demo.collection.dto;

import com._2cha.demo.collection.domain.Collection;
import com._2cha.demo.review.dto.ReviewResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class CollectionDetailResponse {

  private Long id;
  private String title;
  private String description;
  private String thumbnail;
  private List<ReviewResponse> reviews;


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

  public CollectionDetailResponse(Collection collection, List<ReviewResponse> reviews,
                                  String baseUrl) {
    this.id = collection.getId();
    this.title = collection.getTitle();
    this.description = collection.getDescription();
    String path = collection.getThumbnailUrlPath();
    this.thumbnail = path != null ? baseUrl + path : null;
    this.reviews = reviews;
  }
}

