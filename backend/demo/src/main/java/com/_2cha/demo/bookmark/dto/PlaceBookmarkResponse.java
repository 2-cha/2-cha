package com._2cha.demo.bookmark.dto;

import com._2cha.demo.place.dto.PlaceBriefResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlaceBookmarkResponse {

  private Long id;
  private PlaceBriefResponse place;
}
