package com._2cha.demo.place.dto;

import com._2cha.demo.place.domain.Category;
import com._2cha.demo.place.domain.Place;
import com._2cha.demo.review.dto.TagCountResponse;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceBriefResponse {


  private Long id;
  private String name;
  private Category category;
  private String address;
  private String thumbnail;
  private List<TagCountResponse> tagSummary;

  public PlaceBriefResponse(Long id, String name, Category category, String address,
                            String thumbnail) {
    this.id = id;
    this.name = name;
    this.category = category;
    this.address = address;
    this.thumbnail = thumbnail;
  }

  public PlaceBriefResponse(Place place) {
    this.id = place.getId();
    this.name = place.getName();
    this.category = place.getCategory();
    this.address = place.getAddress();
    this.thumbnail = place.getThumbnail();
  }
}
