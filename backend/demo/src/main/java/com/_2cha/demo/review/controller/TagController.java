package com._2cha.demo.review.controller;

import com._2cha.demo.global.annotation.Auth;
import com._2cha.demo.global.annotation.Authed;
import com._2cha.demo.member.domain.Role;
import com._2cha.demo.review.domain.Category;
import com._2cha.demo.review.dto.MakeTagReqRequest;
import com._2cha.demo.review.dto.MakeTagReqResponse;
import com._2cha.demo.review.dto.TagCreationReqBriefResponse;
import com._2cha.demo.review.dto.TagCreationReqDetailResponse;
import com._2cha.demo.review.dto.TagFuzzySearchResponse;
import com._2cha.demo.review.dto.TagFuzzySearchWithoutCategoryResponse;
import com._2cha.demo.review.dto.TagReqAcceptedResponse;
import com._2cha.demo.review.dto.TagReqUpdateRequest;
import com._2cha.demo.review.dto.TagReqUpdatedResponse;
import com._2cha.demo.review.service.TagService;
import com._2cha.demo.review.validator.Hangul;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Auth
@RestController
@RequiredArgsConstructor
@Validated
public class TagController {

  private final TagService tagService;

  @GetMapping("/tags")
  public List<TagFuzzySearchResponse> fuzzySearchTagsByHangul(
      @Hangul @RequestParam(required = false) String query) {
    return tagService.fuzzySearchTagsByHangul(query);
  }

  @GetMapping("/tags/categorized")
  public Map<Category, List<TagFuzzySearchWithoutCategoryResponse>> fuzzySearchCategorizedTagsByHangul(
      @Hangul @RequestParam(required = false) String query) {
    return tagService.fuzzySearchCategorizedTagsByHangul(query);
  }

  @PostMapping("/tags/requests")
  public MakeTagReqResponse makeTagCreationRequest(@Authed Long memberId,
                                                   @RequestBody @Valid MakeTagReqRequest dto) {
    return tagService.makeTagCreationRequest(memberId, dto.getEmoji(), dto.getMessage());
  }

  @Auth(Role.ADMIN)
  @GetMapping("/tags/requests")
  public List<TagCreationReqBriefResponse> getAllTagCreationRequests(Pageable pageParam) {
    return tagService.getAllTagCreationRequests(pageParam);
  }

  @Auth(Role.ADMIN)
  @GetMapping("/tags/requests/{tagReqId}")
  public TagCreationReqDetailResponse getTagCreationRequest(@PathVariable Long tagReqId) {
    return tagService.getTagCreationRequest(tagReqId);
  }

  @Auth(Role.ADMIN)
  @PostMapping("/tags/requests/{tagReqId}/accept")
  public TagReqAcceptedResponse acceptTagCreationRequest(@PathVariable Long tagReqId) {
    return tagService.acceptTagCreationRequest(tagReqId);
  }

  @Auth(Role.ADMIN)
  @DeleteMapping("/tags/requests/{tagReqId}")
  public void rejectTagCreationRequest(@PathVariable Long tagReqId) {
    tagService.rejectTagCreationRequest(tagReqId);
  }

  @Auth(Role.ADMIN)
  @PutMapping("/tags/requests/{tagReqId}")
  public TagReqUpdatedResponse updateTagCreationRequest(@PathVariable Long tagReqId,
                                                        @RequestBody @Valid TagReqUpdateRequest dto) {
    return tagService.updateTagCreationRequest(tagReqId, dto.getCategory(), dto.getEmoji(),
                                               dto.getMessage());
  }
}
