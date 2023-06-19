package com._2cha.demo.review.dto;

import com._2cha.demo.review.domain.Tag;
import java.util.List;
import lombok.Data;

@Data
public class TagFuzzySearchWithoutCategoryResponse {

  private Long id;
  private String emoji;
  private String message;
  private List<Integer> matchingIndexes;

  public TagFuzzySearchWithoutCategoryResponse(Tag tag, List<Integer> matchingIndexes) {
    this.id = tag.getId();
    this.emoji = tag.getEmoji();
    this.message = tag.getMsg();
    this.matchingIndexes = matchingIndexes;
  }
}
