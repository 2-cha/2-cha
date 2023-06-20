package com._2cha.demo.collection.dto;

import com._2cha.demo.place.dto.PlaceBriefResponse;
import com._2cha.demo.review.dto.TagResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ReviewInCollectionResponse {

  private Long id;
  private String image;
  private List<TagResponse> tags = new ArrayList<>();
  private PlaceBriefResponse place;

  public ReviewInCollectionResponse(Long id, String image, PlaceBriefResponse place,
                                    List<TagResponse> tags) {
    this.id = id;
    this.image = image;
    this.place = place;
    this.tags.addAll(tags);
  }
}
