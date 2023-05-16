package com._2cha.demo.place.service;

import com._2cha.demo.global.exception.BadRequestException;
import com._2cha.demo.global.exception.NotFoundException;
import com._2cha.demo.global.infra.imageupload.service.ImageUploadService;
import com._2cha.demo.global.infra.storage.service.FileStorageService;
import com._2cha.demo.place.domain.Category;
import com._2cha.demo.place.domain.Place;
import com._2cha.demo.place.dto.FilterBy;
import com._2cha.demo.place.dto.NearbyPlaceSearchParams;
import com._2cha.demo.place.dto.PlaceBriefResponse;
import com._2cha.demo.place.dto.PlaceBriefWithDistanceResponse;
import com._2cha.demo.place.dto.PlaceCreatedResponse;
import com._2cha.demo.place.dto.PlaceDetailResponse;
import com._2cha.demo.place.dto.PlaceSearchResponse;
import com._2cha.demo.place.dto.PlaceSuggestionResponse;
import com._2cha.demo.place.dto.SortBy;
import com._2cha.demo.place.exception.NoSuchPlaceException;
import com._2cha.demo.place.repository.PlaceQueryRepository;
import com._2cha.demo.place.repository.PlaceRepository;
import com._2cha.demo.review.dto.TagCountResponse;
import com._2cha.demo.review.service.ReviewService;
import com._2cha.demo.util.GeomUtils;
import com._2cha.demo.util.HangulUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceService {

  private final PlaceRepository placeRepository;
  private final PlaceQueryRepository placeQueryRepository;
  private final FileStorageService fileStorageService;
  private final ImageUploadService imageUploadService;
  private static final Integer REVIEW_SUMMARY_SIZE = 3;
  private static final Integer SUGGESTION_SIZE = 10;
  private static final Double SUGGESTION_MAX_DIST = 1000.0;


  private ReviewService reviewService;

  @Autowired
  public void setReviewService(ReviewService reviewService) {
    this.reviewService = reviewService;
  }


  /*-----------
   @ Commands
   ----------*/
  @Transactional
  public PlaceCreatedResponse createPlace(String name, Category category, String address,
                                          String lotAddress,
                                          Double lon, Double lat, List<String> images,
                                          String site) {
    String imgUrlPath = null;
    String thumbUrlPath = null;
    if (!images.isEmpty()) {
      String imgUrl = images.get(0);
      imgUrlPath = fileStorageService.extractPath(imgUrl);
      thumbUrlPath = imageUploadService.getThumbnailPath(imgUrlPath);
    }
    Place place = Place.createPlace(name, category, address, lotAddress, lon, lat,
                                    imgUrlPath, thumbUrlPath, site);
    placeRepository.save(place);
    return new PlaceCreatedResponse(place, fileStorageService.getBaseUrl());
  }

  @Transactional
  public void addPlaceImageUrls(Long placeId, List<String> imageUrls) {
    if (imageUrls.isEmpty()) return;
    Place place = placeRepository.findById(placeId);
    if (place == null) throw new NoSuchPlaceException();

    String url = imageUrls.get(0);  //TODO: Image List
    String imgUrlPath = fileStorageService.extractPath(url);
    String thumbUrlPath = imageUploadService.getThumbnailPath(imgUrlPath);

    place.updateImage(imgUrlPath, thumbUrlPath);  // TODO: add to image list, not replace
  }

  /*-----------
   @ Queries
   ----------*/
  public Place findPlaceById(Long id) {
    Place place = placeRepository.findById(id);
    if (place == null) {
      throw new NotFoundException("No place with id " + id, "noSuchPlace");
    }
    return place;
  }

  public PlaceBriefWithDistanceResponse getPlaceBriefWithDistance(Long placeId, Double lat,
                                                                  Double lon, Integer summarySize) {
    Point cur = GeomUtils.createPoint(lat, lon);
    PlaceBriefWithDistanceResponse brief = placeQueryRepository.getPlaceBriefWithDistance(placeId,
                                                                                          cur,
                                                                                          fileStorageService.getBaseUrl());
    if (brief == null) throw new NotFoundException("No place with id " + placeId, "noSuchPlace");

    brief.setTagSummary(reviewService.getReviewTagCountByPlaceId(placeId, summarySize));

    return brief;
  }


  public PlaceBriefResponse getPlaceBriefById(Long placeId, Integer summarySize) {

    PlaceBriefResponse brief = placeQueryRepository.getPlaceBriefById(placeId,
                                                                      fileStorageService.getBaseUrl());
    if (brief == null) throw new NotFoundException("No place with id " + placeId, "noSuchPlace");

    brief.setTagSummary(reviewService.getReviewTagCountByPlaceId(placeId, summarySize));

    return brief;
  }

  public List<PlaceBriefResponse> getPlacesBriefByIdIn(List<Long> placeIds, Integer summarySize) {
    List<PlaceBriefResponse> briefs = placeQueryRepository.getPlacesBriefsByIdIn(placeIds,
                                                                                 fileStorageService.getBaseUrl());

    Map<Long, List<TagCountResponse>> placesTagCounts = reviewService.getReviewTagCountsByPlaceIdIn(
        briefs.stream().map(place -> place.getId()).toList(), summarySize);

    briefs.forEach(brief -> brief.setTagSummary(placesTagCounts.get(brief.getId())));

    return briefs;
  }

  public PlaceDetailResponse getPlaceDetailById(Long id) {
    PlaceDetailResponse detail = placeQueryRepository.getPlaceDetailById(id,
                                                                         fileStorageService.getBaseUrl());
    if (detail == null) throw new NotFoundException("No place with id " + id, "noSuchPlace");

    detail.setTags(reviewService.getReviewTagCountByPlaceId(id, null));

    return detail;
  }

  public List<PlaceSuggestionResponse> suggestNearbyPlaces(Double lat, Double lon) {

    List<Object[]> placesWithDist =
        placeQueryRepository.findAround(new NearbyPlaceSearchParams(lat, lon,
                                                                    SUGGESTION_MAX_DIST,
                                                                    FilterBy.DEFAULT, null,
                                                                    SortBy.DISTANCE,
                                                                    0L, SUGGESTION_SIZE));

    return placesWithDist.stream()
                         .map(pd -> new PlaceSuggestionResponse(
                             (Place) pd[0], (Double) pd[1]))
                         .toList();
  }

  public List<PlaceBriefWithDistanceResponse>
  searchPlacesWithFilterAndSorting(NearbyPlaceSearchParams searchParams) {
    if (searchParams.getSortBy() == SortBy.TAG_COUNT &&
        searchParams.getFilterBy() != FilterBy.TAG) {
      throw new BadRequestException("Sorting by tag count is only allowed with tag filter",
                                    "badSortStrategy");
    }
    List<Object[]> placesWithDist = placeQueryRepository.findAround(searchParams);
    List<Place> places = new ArrayList<>();
    List<Double> distances = new ArrayList<>();

    placesWithDist.stream().forEach(placeWithDist -> {
      places.add((Place) placeWithDist[0]);
      distances.add((Double) placeWithDist[1]);
    });

    Map<Long, List<TagCountResponse>> placesTagCounts = reviewService.getReviewTagCountsByPlaceIdIn(
        places.stream().map(place -> place.getId()).toList(), REVIEW_SUMMARY_SIZE);

    return placesWithDist.stream().map((placeWithDist) -> {
      Place place = (Place) placeWithDist[0];
      Double distGap = (Double) placeWithDist[1];
      PlaceBriefWithDistanceResponse brief = new PlaceBriefWithDistanceResponse(place, distGap,
                                                                                fileStorageService.getBaseUrl());
      brief.setTagSummary(placesTagCounts.get(place.getId()));
      return brief;
    }).toList();
  }

  public List<PlaceSearchResponse> fuzzySearch(String queryText, Pageable pageParam) {
    String queryRegex = this.makeQueryRegex(queryText);
    String baseUrl = fileStorageService.getBaseUrl();
    List<Place> places = placeRepository.findPlacesByNameMatchesRegex(queryRegex, pageParam);
    return places.stream().map(place -> new PlaceSearchResponse(place, baseUrl)).toList();
  }

  private String makeQueryRegex(String queryText) {

    int i = -1;
    boolean prevSpace = false;
    String queryRegex = "";

    while (++i < queryText.length()) {
      char c = queryText.charAt(i);
      if (HangulUtils.isPartialChar(c)) {
        queryRegex += makeCompleteRange(c);
      } else if (Character.isLetterOrDigit(c)) {
        queryRegex += c;
      } else if (Character.isSpaceChar(c)) {
        prevSpace = true;
      } else {
        continue;
      }
      if (!prevSpace) {
        queryRegex += ".*"; // Fuzzy Matching, to ignore only spaces, use "\\s*".
      }
      prevSpace = false;
    }
    return queryRegex;
  }

  private String makeCompleteRange(char 초성) {
    char start = HangulUtils.makeCompleteChar(초성, 'ㅏ', '\0');
    char end = HangulUtils.makeCompleteChar(초성, 'ㅣ', 'ㅎ');

    return "[" + start + "-" + end + "]";
  }
}
