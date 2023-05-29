package com._2cha.demo.place.dto;

import com._2cha.demo.place.domain.Category;
import com._2cha.demo.place.domain.Place;
import lombok.Data;

@Data
public class PlaceSuggestionResponse {

  private Long id;
  private String name;
  private String address;
  private String lotAddress;
  private Category category;
  private Double distance;

  public PlaceSuggestionResponse(Place place, Double distance) {
    this.id = place.getId();
    this.name = place.getName();
    this.address = place.getAddress();
    this.lotAddress = place.getLotAddress();
    this.category = place.getCategory();
    this.distance = distance;
  }
}
