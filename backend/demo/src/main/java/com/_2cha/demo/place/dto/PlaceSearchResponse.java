package com._2cha.demo.place.dto;

import com._2cha.demo.place.domain.Category;
import com._2cha.demo.place.domain.Place;
import java.util.List;
import lombok.Data;

@Data
public class PlaceSearchResponse {

  private Long id;
  private String name;
  private Category category;
  private String address;
  private String thumbnail;
  private Double lon;
  private Double lat;
  private List<Integer> matchingIndexes;

  public PlaceSearchResponse(Place place, List<Integer> matchingIndexes, String baseUrl) {
    this.id = place.getId();
    this.name = place.getName();
    this.category = place.getCategory();
    this.address = place.getAddress();
    this.thumbnail =
        place.getThumbnailUrlPath() != null ? baseUrl + place.getThumbnailUrlPath() : null;
    this.lon = place.getLocation().getX();
    this.lat = place.getLocation().getY();

    this.matchingIndexes = matchingIndexes;
  }
}
