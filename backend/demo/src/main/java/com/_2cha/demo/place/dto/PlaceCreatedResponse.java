package com._2cha.demo.place.dto;

import com._2cha.demo.place.domain.Category;
import com._2cha.demo.place.domain.Place;
import com._2cha.demo.util.GeomUtils;
import lombok.Data;

@Data
public class PlaceCreatedResponse {

  private Long id;
  private String name;
  private Category category;
  private String address;
  private String lotAddress;
  private String image;
  private Double lon;
  private Double lat;


  public PlaceCreatedResponse(Place place, String baseUrl) {
    this.id = place.getId();
    this.name = place.getName();
    this.category = place.getCategory();
    this.address = place.getAddress();
    this.lotAddress = place.getLotAddress();
    this.image = place.getImageUrlPath() != null ? baseUrl + place.getImageUrlPath() : null;
    this.lat = GeomUtils.lat(place.getLocation());
    this.lon = GeomUtils.lon(place.getLocation());
  }
}
