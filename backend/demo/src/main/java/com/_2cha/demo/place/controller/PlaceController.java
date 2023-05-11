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
import org.springframework.data.domain.Pageable;
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

  @GetMapping("/places/{placeId}")
  public PlaceDetailResponse getPlaceDetailById(@PathVariable Long placeId) {
    return placeService.getPlaceDetailById(placeId);
  }

  @GetMapping("/places/nearby")
  public List<PlaceBriefWithDistanceResponse> getNearbyPlace(
      @RequestParam(name = "filter_by", required = false, defaultValue = "default") FilterBy filterBy,
      @RequestParam(name = "filter_values", required = false) List<String> filterValues,
      @RequestParam(name = "sort_by", required = false, defaultValue = "distance") SortBy sortBy,
      @RequestParam(name = "max_dist") Double maxDist,
      @RequestParam(name = "lat") Double lat,
      @RequestParam(name = "lon") Double lon,
      @RequestParam Map<String, Object> params,
      Pageable pageParam) {

    params.put("filter_by", filterBy);
    params.put("sort_by", sortBy);
    params.put("filter_values", filterValues);
    NearbyPlaceRequest dto = objectMapper.convertValue(params, NearbyPlaceRequest.class);

    return placeService.searchPlacesWithFilterAndSorting(dto.getLat(), dto.getLon(),
                                                         dto.getMaxDist(),
                                                         pageParam.getOffset(),
                                                         pageParam.getPageSize(),
                                                         dto.getSortBy(), dto.getFilterBy(),
                                                         dto.getFilterValues()
                                                        );
  }
}
