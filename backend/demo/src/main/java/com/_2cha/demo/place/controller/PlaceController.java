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

@Auth
@RestController
@RequiredArgsConstructor
public class PlaceController {

  private final PlaceService placeService;
  private final ObjectMapper objectMapper;
  private static final String DEFAULT_PAGE_SIZE = "10";

  @GetMapping("/places/{placeId}")
  public PlaceDetailResponse getPlaceDetailById(@PathVariable Long placeId) {
    return placeService.getPlaceDetailById(placeId);
  }

  @GetMapping("/places/nearby")
  public List<PlaceBriefWithDistanceResponse> getNearbyPlace(
      @RequestParam(name = "filter_by", required = false, defaultValue = "default") FilterBy filterBy,
      @RequestParam(name = "filter_values", required = false) List<String> filterValues,
      @RequestParam(name = "sort_by", required = false, defaultValue = "distance") SortBy sortBy,
      @RequestParam(name = "page_size", required = false, defaultValue = "10") Integer pageSize,
      @RequestParam(name = "min_dist") Double minDist,
      @RequestParam(name = "max_dist") Double maxDist,
      @RequestParam(name = "lat") Double lat,
      @RequestParam(name = "lon") Double lon,
      @RequestParam Map<String, Object> params) {

    params.put("filter_by", filterBy);
    params.put("sort_by", sortBy);
    params.put("page_size", pageSize);
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
