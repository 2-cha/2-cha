package com._2cha.demo.place.dto;

import com._2cha.demo.place.domain.Category;
import com._2cha.demo.place.domain.Place;
import lombok.Data;
import org.locationtech.jts.geom.Point;

@Data
public class PlaceSearchResponse {

  private Long id;
  private String name;
  private Category category;
  private String address;
  private String thumbnail;
  private Double lon;
  private Double lat;

  public PlaceSearchResponse(Place place, String baseUrl) {
    this.id = place.getId();
    this.name = place.getName();
    this.category = place.getCategory();
    this.address = place.getAddress();
    this.thumbnail =
        place.getThumbnailUrlPath() != null ? baseUrl + place.getThumbnailUrlPath() : null;
    this.lon = place.getLocation().getX();
    this.lat = place.getLocation().getY();
  }

  public PlaceSearchResponse(Long id, String name, Category category, String address,
                             String thumbnailUrlPath,
                             Point location) {
    this.id = id;
    this.name = name;
    this.category = category;
    this.address = address;
    this.thumbnail = thumbnailUrlPath;
    this.lon = location.getX();
    this.lat = location.getY();
  }
}
