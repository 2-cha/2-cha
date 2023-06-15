package com._2cha.demo.place.controller;

import static com._2cha.demo.member.domain.Role.GUEST;
import static com._2cha.demo.member.domain.Role.MEMBER;

import com._2cha.demo.global.annotation.Auth;
import com._2cha.demo.global.annotation.Authed;
import com._2cha.demo.global.infra.imageupload.service.ImageUploadService;
import com._2cha.demo.member.domain.Role;
import com._2cha.demo.place.dto.FilterBy;
import com._2cha.demo.place.dto.NearbyPlaceSearchParams;
import com._2cha.demo.place.dto.PlaceBriefResponse;
import com._2cha.demo.place.dto.PlaceBriefWithDistanceResponse;
import com._2cha.demo.place.dto.PlaceCreatedResponse;
import com._2cha.demo.place.dto.PlaceDetailResponse;
import com._2cha.demo.place.dto.PlaceEnrollRequest;
import com._2cha.demo.place.dto.PlaceSearchResponse;
import com._2cha.demo.place.dto.SortBy;
import com._2cha.demo.place.dto.SortOrder;
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

@RestController
@RequiredArgsConstructor
public class PlaceController {

  private final PlaceService placeService;
  private final ImageUploadService imageUploadService;

  @Auth(GUEST)
  @GetMapping("/places/{placeId}")
  public PlaceDetailResponse getPlaceDetailById(@Authed Long memberId, @PathVariable Long placeId) {
    PlaceDetailResponse place = placeService.getPlaceDetailById(placeId);
    placeService.setResponseBookmarkStatus(memberId, place);
    return place;
  }

  @Auth(MEMBER)
  @GetMapping("/places")
  public List<PlaceSearchResponse> searchPlaceByName(@RequestParam String query,
                                                     Pageable pageParam) {
    return placeService.fuzzySearch(query, pageParam);
  }

  @Auth(GUEST)
  @GetMapping("/places/nearby")
  public List<PlaceBriefWithDistanceResponse> getNearbyPlace(
      @Authed Long memberId,
      @RequestParam(name = "filter_by", required = false, defaultValue = "default") FilterBy filterBy,
      @RequestParam(name = "filter_values", required = false) List<String> filterValues,
      @RequestParam(name = "sort_by", required = false, defaultValue = "distance") SortBy sortBy,
      @RequestParam(name = "sort_order", required = false, defaultValue = "asc") SortOrder sortOrder,
      @RequestParam(name = "max_dist") Double maxDist,
      @RequestParam(name = "lat") Double lat,
      @RequestParam(name = "lon") Double lon,
      Pageable pageParam) {
    NearbyPlaceSearchParams searchParams = new NearbyPlaceSearchParams(lat, lon, maxDist,
                                                                       filterBy, filterValues,
                                                                       sortBy, sortOrder,
                                                                       pageParam.getOffset(),
                                                                       pageParam.getPageSize()
    );
    List<PlaceBriefWithDistanceResponse> places = placeService.searchPlacesWithFilterAndSorting(
        searchParams);
    placeService.setResponseBookmarkStatus(memberId, places);
    return places;
  }

  @Auth(MEMBER)
  @PostMapping("/places")
  public PlaceCreatedResponse createPlace(@RequestBody @Valid PlaceEnrollRequest dto) {
    return placeService.createPlace(dto.getName(), dto.getCategory(), dto.getAddress(),
                                    dto.getLotAddress(), dto.getLon(), dto.getLat(), dto.getSite());
  }

  @Auth(Role.MEMBER)
  @PutMapping("/places/{placeId}")
  public void updatePlace(@RequestBody @Valid PlaceEnrollRequest dto) {
    //TODO
  }

  @Auth(Role.MEMBER)
  @GetMapping("/bookmarks/places")
  public List<PlaceBriefResponse> getBookmarkedPlaces(@Authed Long memberId) {
    return placeService.getBookmarkedPlaces(memberId);
  }

  @Auth(Role.MEMBER)
  @PostMapping("/bookmarks/places/{placeId}")
  public void createBookmark(@Authed Long memberId, @PathVariable Long placeId) {
    placeService.createBookmark(memberId, placeId);
  }

  @Auth(Role.MEMBER)
  @DeleteMapping("/bookmarks/places/{placeId}")
  public void removeBookmark(@Authed Long memberId, @PathVariable Long placeId) {
    placeService.removeBookmark(memberId, placeId);
  }
}
