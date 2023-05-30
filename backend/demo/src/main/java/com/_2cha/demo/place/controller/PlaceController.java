package com._2cha.demo.place.controller;

import com._2cha.demo.global.annotation.Auth;
import com._2cha.demo.global.annotation.Authed;
import com._2cha.demo.global.infra.imageupload.service.ImageUploadService;
import com._2cha.demo.place.dto.FilterBy;
import com._2cha.demo.place.dto.NearbyPlaceSearchParams;
import com._2cha.demo.place.dto.PlaceBriefResponse;
import com._2cha.demo.place.dto.PlaceBriefWithDistanceResponse;
import com._2cha.demo.place.dto.PlaceCreatedResponse;
import com._2cha.demo.place.dto.PlaceDetailResponse;
import com._2cha.demo.place.dto.PlaceEnrollRequest;
import com._2cha.demo.place.dto.PlaceSearchResponse;
import com._2cha.demo.place.dto.SortBy;
import com._2cha.demo.place.service.PlaceService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
public class PlaceController {

  private final PlaceService placeService;
  private final ImageUploadService imageUploadService;

  @GetMapping("/places/{placeId}")
  public PlaceDetailResponse getPlaceDetailById(@PathVariable Long placeId) {
    return placeService.getPlaceDetailById(placeId);
  }

  @GetMapping("/places")
  public List<PlaceSearchResponse> searchPlaceByName(@RequestParam String query,
                                                     Pageable pageParam) {
    return placeService.fuzzySearch(query, pageParam);
  }

  @GetMapping("/places/nearby")
  public List<PlaceBriefWithDistanceResponse> getNearbyPlace(
      @RequestParam(name = "filter_by", required = false, defaultValue = "default") FilterBy filterBy,
      @RequestParam(name = "filter_values", required = false) List<String> filterValues,
      @RequestParam(name = "sort_by", required = false, defaultValue = "distance") SortBy sortBy,
      @RequestParam(name = "max_dist") Double maxDist,
      @RequestParam(name = "lat") Double lat,
      @RequestParam(name = "lon") Double lon,
      Pageable pageParam) {
    NearbyPlaceSearchParams searchParams = new NearbyPlaceSearchParams(lat, lon, maxDist, filterBy,
                                                                       filterValues, sortBy,
                                                                       pageParam.getOffset(),
                                                                       pageParam.getPageSize()
    );
    return placeService.searchPlacesWithFilterAndSorting(searchParams);
  }

  @PostMapping("/places")
  public PlaceCreatedResponse createPlace(@RequestBody @Valid PlaceEnrollRequest dto) {
    return placeService.createPlace(dto.getName(), dto.getCategory(), dto.getAddress(),
                                    dto.getLotAddress(), dto.getLon(), dto.getLat(), dto.getSite());
  }

  @PutMapping("/places/{placeId}")
  public void updatePlace(@RequestBody @Valid PlaceEnrollRequest dto) {
    //TODO
  }

  @GetMapping("/bookmarks/places")
  public List<PlaceBriefResponse> getBookmarkedPlaces(@Authed Long memberId) {
    return placeService.getBookmarkedPlaces(memberId);
  }

  @PostMapping("/bookmarks/places/{placeId}")
  public void createBookmark(@Authed Long memberId, @PathVariable Long placeId) {
    placeService.createBookmark(memberId, placeId);
  }

  @DeleteMapping("/bookmarks/places/{placeId}")
  public void removeBookmark(@Authed Long memberId, @PathVariable Long placeId) {
    placeService.removeBookmark(memberId, placeId);
  }
}
