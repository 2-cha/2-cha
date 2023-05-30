package com._2cha.demo.place.dto;

import com._2cha.demo.place.domain.Category;
import com._2cha.demo.review.dto.TagCountResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

@Data
public class PlaceDetailResponse {

  private Long id;
  private String name;
  private Category category;
  private String address;
  private String lotAddress;
  private String image;
  private String site;
  private Double lat;
  private Double lon;
  private List<TagCountResponse> tags;

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

  public PlaceDetailResponse(Long id, String name, Category category, String address,
                             String lotAddress,
                             String imageUrl, String site, Point location) {
    this.id = id;
    this.name = name;
    this.category = category;
    this.lotAddress = lotAddress;
    this.address = address;
    this.image = imageUrl;
    this.site = site;
    this.lat = location.getY();
    this.lon = location.getX();
  }
}
