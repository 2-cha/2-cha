package com._2cha.demo.place.dto;

import com._2cha.demo.place.domain.Category;
import com._2cha.demo.place.domain.Place;
import com._2cha.demo.review.dto.TagCountResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceBriefResponse {


  private Long id;
  private String name;
  private Category category;
  private String address;
  private String image;
  private List<TagCountResponse> tagSummary;

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

  public PlaceBriefResponse(Long id, String name, Category category, String address,
                            String imageUrl) {
    this.id = id;
    this.name = name;
    this.category = category;
    this.address = address;
    this.image = imageUrl;
  }

  public PlaceBriefResponse(Place place, String imageBaseUrl) {
    this.id = place.getId();
    this.name = place.getName();
    this.category = place.getCategory();
    this.address = place.getAddress();
    String path = place.getImageUrlPath();
    this.image = path != null ? imageBaseUrl + path : null;
  }
}
