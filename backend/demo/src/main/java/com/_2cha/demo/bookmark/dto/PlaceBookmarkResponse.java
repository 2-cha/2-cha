package com._2cha.demo.bookmark.dto;

import com._2cha.demo.bookmark.domain.PlaceBookmark;
import com._2cha.demo.place.dto.PlaceBriefResponse;
import lombok.Data;

@Data

public class PlaceBookmarkResponse {

  private Long id;
  private PlaceBriefResponse place;

  public PlaceBookmarkResponse(PlaceBookmark bookmark) {
    this.id = bookmark.getId();
    this.place = new PlaceBriefResponse(bookmark.getItem());
  }
}
