package com._2cha.demo.place.controller;

import com._2cha.demo.global.annotation.Auth;
import com._2cha.demo.place.domain.Category;
import com._2cha.demo.place.dto.NearbyPlaceRequest;
import com._2cha.demo.place.dto.NearbyPlaceWithCategoryFilterRequest;
import com._2cha.demo.place.dto.NearbyPlaceWithTagFilterRequest;
import com._2cha.demo.place.dto.PlaceBriefWithDistanceResponse;
import com._2cha.demo.place.dto.PlaceDetailResponse;
import com._2cha.demo.place.service.PlaceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PlaceController {

  private final PlaceService placeService;
  private final ObjectMapper objectMapper;


  @Auth
  @GetMapping("/places/{placeId}")
  public PlaceDetailResponse getPlaceDetailById(@PathVariable Long placeId) {
    return placeService.getPlaceDetailById(placeId);
  }

  //  @Auth(Role.ADMIN)
  @GetMapping("/places/nearby")
  public List<PlaceBriefWithDistanceResponse> getNearbyPlace(
      @RequestParam Map<String, String> params) {

    NearbyPlaceRequest dto = objectMapper.convertValue(params, NearbyPlaceRequest.class);

    return placeService.getNearbyPlace(dto.getLat(), dto.getLon(),
                                       dto.getMinDist(), dto.getMaxDist(), dto.getPageSize());
  }

  @GetMapping("/places/nearby/tag")
  public List<PlaceBriefWithDistanceResponse> getNearbyPlaceWithTagFilter(
      @RequestParam(name = "tag_ids") List<Long> tagIds,
      @RequestParam Map<String, Object> params) {

    params.put("tag_ids", tagIds);
    NearbyPlaceWithTagFilterRequest dto = objectMapper.convertValue(params,
                                                                    NearbyPlaceWithTagFilterRequest.class);

    return placeService.getNearbyPlaceWithTagFilter(dto.getLat(), dto.getLon(),
                                                    dto.getMinDist(), dto.getMaxDist(),
                                                    dto.getPageSize(), dto.getTagIds());
  }

  @GetMapping("/places/nearby/category")
  public List<PlaceBriefWithDistanceResponse> getNearbyPlaceWithCategoryFilter(
      @RequestParam(name = "categories") List<Category> categories,
      @RequestParam Map<String, Object> params) {

    params.put("categories", categories);
    NearbyPlaceWithCategoryFilterRequest dto = objectMapper.convertValue(params,
                                                                         NearbyPlaceWithCategoryFilterRequest.class);

    return placeService.getNearbyPlaceWithCategoryFilter(dto.getLat(), dto.getLon(),
                                                         dto.getMinDist(), dto.getMaxDist(),
                                                         dto.getPageSize(), dto.getCategories());
  }
}
