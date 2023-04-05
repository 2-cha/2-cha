package com._2cha.demo.review.dto;

import com._2cha.demo.review.domain.Review;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public abstract class ReviewResponse {

  List<TagResponse> tags;
  List<String> images;

  public ReviewResponse fetchTagsAndImagesFromReview(Review review) {

    List<String> imgUrls = new ArrayList<>();
    List<TagResponse> tagResponses = new ArrayList<>();
    review.getImages().forEach(img -> imgUrls.add(img.getUrl()));
    review.getTags().forEach(
        tag -> {
          TagResponse tagResponse = new TagResponse();
          tagResponse.setEmoji(tag.getEmoji());
          tagResponse.setMessage(tag.getMsg());
          tagResponses.add(tagResponse);
        });
    this.setTags(tagResponses);
    this.setImages(imgUrls);

    return this;
  }
}
