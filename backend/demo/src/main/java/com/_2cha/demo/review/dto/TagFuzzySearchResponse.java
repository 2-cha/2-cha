package com._2cha.demo.review.dto;

import com._2cha.demo.review.domain.Category;
import com._2cha.demo.review.domain.Tag;
import java.util.List;
import lombok.Data;

@Data
public class TagFuzzySearchResponse {

  private Long id;
  private String emoji;
  private String message;
  private Category category;
  private List<Integer> matchingIndexes;

  public TagFuzzySearchResponse(Tag tag, List<Integer> matchingIndexes) {
    this.id = tag.getId();
    this.emoji = tag.getEmoji();
    this.message = tag.getMsg();
    this.category = tag.getCategory();
    this.matchingIndexes = matchingIndexes;
  }
}
