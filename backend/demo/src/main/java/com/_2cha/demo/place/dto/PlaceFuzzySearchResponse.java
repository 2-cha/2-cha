package com._2cha.demo.place.dto;

import com._2cha.demo.place.domain.Category;
import com._2cha.demo.place.domain.Place;
import com._2cha.demo.util.GeomUtils;
import java.util.List;
import lombok.Data;

@Data
public class PlaceFuzzySearchResponse {

  private Long id;
  private String name;
  private Category category;
  private String address;
  private String thumbnail;
  private Double lon;
  private Double lat;
  private List<Integer> matchingIndexes;

  public PlaceFuzzySearchResponse(Place place, List<Integer> matchingIndexes, String baseUrl) {
    this.id = place.getId();
    this.name = place.getName();
    this.category = place.getCategory();
    this.address = place.getAddress();
    this.thumbnail =
        place.getThumbnailUrlPath() != null ? baseUrl + place.getThumbnailUrlPath() : null;
    this.lon = GeomUtils.lon(place.getLocation());
    this.lat = GeomUtils.lat(place.getLocation());

    this.matchingIndexes = matchingIndexes;
  }
}
