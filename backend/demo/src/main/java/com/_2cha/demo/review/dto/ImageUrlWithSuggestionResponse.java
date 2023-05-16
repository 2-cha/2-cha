package com._2cha.demo.review.dto;

import com._2cha.demo.place.dto.PlaceSuggestionResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageUrlWithSuggestionResponse {

  private String url;
  private List<PlaceSuggestionResponse> suggestions;
}
