package com._2cha.demo.place.dto;

import com._2cha.demo.place.domain.Category;
import com._2cha.demo.place.domain.Place;
import lombok.Data;

@Data
public class PlaceBriefWithDistanceResponse extends PlaceBriefResponse {

  private Double distance;

  public PlaceBriefWithDistanceResponse(Long id, String name,
                                        Category category, String address,
                                        String imageUrl, Double distance) {
    super(id, name, category, address, imageUrl);
    this.distance = distance;
  }

  public PlaceBriefWithDistanceResponse(Place place, Double distance, String imageBaseUrl) {
    super(place, imageBaseUrl);
    this.distance = distance;
  }
}
