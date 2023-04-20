package com._2cha.demo.review.dto;

import com._2cha.demo.review.domain.Review;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ReviewResponse {

  private Long id;
  private List<TagResponse> tags = new ArrayList<>();
  private List<String> images = new ArrayList<>();

  public ReviewResponse(Review review) {
    review.getImages().forEach(img -> images.add(img.getUrl()));
    review.getTags().forEach(tag -> tags.add(new TagResponse(tag)));
  }
}
