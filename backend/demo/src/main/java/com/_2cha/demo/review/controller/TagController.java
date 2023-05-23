package com._2cha.demo.review.controller;

import com._2cha.demo.global.annotation.Auth;
import com._2cha.demo.review.domain.Category;
import com._2cha.demo.review.dto.TagWithIdResponse;
import com._2cha.demo.review.dto.TagWithoutCategoryResponse;
import com._2cha.demo.review.service.TagService;
import com._2cha.demo.review.validator.Hangul;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Auth
@RestController
@RequiredArgsConstructor
@Validated
public class TagController {

  private final TagService tagService;

  @GetMapping("/tags")
  public List<TagWithIdResponse> fuzzySearchTagsByHangul(@Hangul @RequestParam String query) {
    return tagService.fuzzySearchTagsByHangul(query);
  }

  @GetMapping("/tags/categorized")
  public Map<Category, List<TagWithoutCategoryResponse>> fuzzySearchCategorizedTagsByHangul(
      @Hangul @RequestParam String query) {
    return tagService.fuzzySearchCategorizedTagsByHangul(query);
  }
}
