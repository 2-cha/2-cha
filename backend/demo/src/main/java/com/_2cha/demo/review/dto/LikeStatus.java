package com._2cha.demo.review.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class LikeStatus {

  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private Boolean isLiked;

  private Long count;

  @JsonProperty(value = "is_liked")
  public Boolean isLiked() {
    return isLiked;
  }

  public void setLiked(Boolean liked) {
    isLiked = liked;
  }
}
