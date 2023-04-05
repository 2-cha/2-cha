package com._2cha.demo.place.dto;

import com._2cha.demo.place.domain.Category;
import com._2cha.demo.review.dto.TagCountResponse;
import java.util.List;
import lombok.Data;

@Data
public class PlaceDetailResponse {

  private Long id;
  private String name;
  private Category category;
  private String address;
  private String thumbnail;
  private List<TagCountResponse> tags;
}
