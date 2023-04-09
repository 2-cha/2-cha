package com._2cha.demo.place.controller;

import com._2cha.demo.global.annotation.Auth;
import com._2cha.demo.place.dto.FilterBy;
import com._2cha.demo.place.dto.NearbyPlaceRequest;
import com._2cha.demo.place.dto.PlaceBriefWithDistanceResponse;
import com._2cha.demo.place.dto.PlaceDetailResponse;
import com._2cha.demo.place.dto.SortBy;
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

  @GetMapping("/places/nearby")
  public List<PlaceBriefWithDistanceResponse> getNearbyPlaceTest(
      @RequestParam(name = "filter_values", required = false) List<String> filterValues,
      @RequestParam Map<String, Object> params) {

    params.put("filter_by", FilterBy.valueOf(((String) params.get("filter_by")).toUpperCase()));
    params.put("sort_by", SortBy.valueOf(((String) params.get("sort_by")).toUpperCase()));
    params.put("filter_values", filterValues);
    NearbyPlaceRequest dto = objectMapper.convertValue(params, NearbyPlaceRequest.class);
    return placeService.searchPlacesWithFilterAndSorting(dto.getLat(), dto.getLon(),
                                                         dto.getMinDist(), dto.getMaxDist(),
                                                         dto.getPageSize(),
                                                         dto.getSortBy(), dto.getFilterBy(),
                                                         dto.getFilterValues()
                                                        );
  }
}
