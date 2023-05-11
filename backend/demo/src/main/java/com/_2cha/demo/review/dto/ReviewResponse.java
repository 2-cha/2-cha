package com._2cha.demo.review.dto;

import com._2cha.demo.member.dto.MemberProfileResponse;
import com._2cha.demo.place.dto.PlaceBriefResponse;
import com._2cha.demo.review.domain.Review;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(Include.NON_NULL)
public class ReviewResponse {

  private Long id;
  private List<TagResponse> tags = new ArrayList<>();
  private List<String> images = new ArrayList<>();
  private MemberProfileResponse member;
  private PlaceBriefResponse place;

  public ReviewResponse(Review review, MemberProfileResponse member, PlaceBriefResponse place,
                        String imgBaseUrl) {
    review.getImages().forEach(img -> {
      String path = img.getUrlPath();
      this.images.add(path != null ? imgBaseUrl + img.getUrlPath() : null);
    });

    review.getTags().forEach(tag -> this.tags.add(new TagResponse(tag)));

    this.id = review.getId();
    this.member = member;
    this.place = place;
  }
}
