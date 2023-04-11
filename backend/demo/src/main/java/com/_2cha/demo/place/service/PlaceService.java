package com._2cha.demo.place.service;

import com._2cha.demo.global.exception.BadRequestException;
import com._2cha.demo.global.exception.NotFoundException;
import com._2cha.demo.place.domain.Category;
import com._2cha.demo.place.domain.Place;
import com._2cha.demo.place.dto.FilterBy;
import com._2cha.demo.place.dto.PlaceBriefResponse;
import com._2cha.demo.place.dto.PlaceBriefWithDistanceResponse;
import com._2cha.demo.place.dto.PlaceDetailResponse;
import com._2cha.demo.place.dto.SortBy;
import com._2cha.demo.place.repository.PlaceRepository;
import com._2cha.demo.review.dto.TagCountResponse;
import com._2cha.demo.review.service.ReviewService;
import com._2cha.demo.util.GeomUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceService {

  private final PlaceRepository placeRepository;
  private final Integer REVIEW_SUMMARY_SIZE = 3;


  private ReviewService reviewService;

  @Autowired
  public void setReviewService(ReviewService reviewService) {
    this.reviewService = reviewService;
  }

  public Place getPlaceById(Long id) {
    Place place = placeRepository.findById(id);
    if (place == null) {
      throw new NotFoundException("No place with id " + id, "noSuchPlace");
    }
    return place;
  }

  public PlaceBriefWithDistanceResponse getPlaceBriefWithDistance(Long id, Double lon, Double lat) {
    Place place = placeRepository.findById(id);
    if (place == null) {
      throw new NotFoundException("No place with id " + id, "noSuchPlace");
    }

    PlaceBriefWithDistanceResponse dto = new PlaceBriefWithDistanceResponse();
    dto.setId(id);
    dto.setName(place.getName());
    dto.setCategory(place.getCategory());
    dto.setAddress(place.getAddress());
    dto.setThumbnail(place.getThumbnail());
    Point cur = GeomUtils.createPoint(lat, lon);
    dto.setDistance(cur.distance(place.getLocation()));

    return dto;
  }


  public PlaceBriefResponse getPlaceBriefById(Long placeId, Integer summarySize) {
    Place place = placeRepository.findById(placeId);
    if (place == null) {
      throw new NotFoundException("No place with id " + placeId, "noSuchPlace");
    }

    PlaceBriefResponse dto = new PlaceBriefResponse();
    dto.setId(placeId);
    dto.setName(place.getName());
    dto.setCategory(place.getCategory());
    dto.setAddress(place.getAddress());
    dto.setThumbnail(place.getThumbnail());
    dto.setTagSummary(reviewService.getReviewTagCountByPlaceId(placeId, summarySize));

    return dto;
  }

  public List<PlaceBriefResponse> getPlacesBriefByIdIn(List<Long> placeIds, Integer summarySize) {
    List<Place> places = placeRepository.findByIdIn(placeIds);
    List<PlaceBriefResponse> dtos = new ArrayList<>();
    if (places.isEmpty()) {
      throw new NotFoundException("No place with id " + placeIds, "noSuchPlace");
    }
    Map<Long, List<TagCountResponse>> placesTagCounts = reviewService.getReviewTagCountsByPlaceIdIn(
        places.stream().map(place -> place.getId()).toList(), REVIEW_SUMMARY_SIZE);
    
    places.forEach(place -> {
      PlaceBriefResponse dto = new PlaceBriefResponse();
      dto.setId(place.getId());
      dto.setName(place.getName());
      dto.setCategory(place.getCategory());
      dto.setAddress(place.getAddress());
      dto.setThumbnail(place.getThumbnail());
      dto.setTagSummary(placesTagCounts.get(place.getId()));
      dtos.add(dto);
    });

    return dtos;
  }

  public PlaceDetailResponse getPlaceDetailById(Long id) {
    Place place = placeRepository.findById(id);
    if (place == null) {
      throw new NotFoundException("No place with id " + id, "noSuchPlace");
    }
    PlaceDetailResponse dto = new PlaceDetailResponse();
    dto.setId(id);
    dto.setName(place.getName());
    dto.setCategory(place.getCategory());
    dto.setAddress(place.getAddress());
    dto.setThumbnail(place.getThumbnail());
    dto.setTags(reviewService.getReviewTagCountByPlaceId(id, null));

    return dto;
  }

  public List<PlaceBriefWithDistanceResponse>
  searchPlacesWithFilterAndSorting(Double lat, Double lon, Double minDist, Double maxDist,
                                   Integer pageSize,
                                   SortBy sortBy, FilterBy filterBy, List<String> filterValues) {

    if (sortBy == SortBy.TAG_COUNT && filterBy != FilterBy.TAG) {
      throw new BadRequestException("Sorting by tag count is only allowed with tag filter",
                                    "badSortStrategy");
    }
    List<Object> convertedFilterValues = new ArrayList<>();

    if (filterValues != null) {
      for (var val : filterValues) {
        Object converted = switch (filterBy) {
          case DEFAULT -> val;
          case TAG -> Long.valueOf(val);
          case CATEGORY -> Category.valueOf(val);
        };
        convertedFilterValues.add(converted);
      }
    }
    List<Object[]> placesWithDist = placeRepository.findAround(lat, lon, minDist, maxDist, pageSize,
                                                               sortBy, filterBy,
                                                               convertedFilterValues);
    List<Place> places = new ArrayList<>();
    List<Double> distances = new ArrayList<>();

    placesWithDist.stream().forEach(placeWithDist -> {
      places.add((Place) placeWithDist[0]);
      distances.add((Double) placeWithDist[1]);
    });

    Map<Long, List<TagCountResponse>> placesTagCounts = reviewService.getReviewTagCountsByPlaceIdIn(
        places.stream().map(place -> place.getId()).toList(), REVIEW_SUMMARY_SIZE);

    return placesWithDist.stream().map((placeWithDist) -> {
      PlaceBriefWithDistanceResponse brief = new PlaceBriefWithDistanceResponse();
      Place place = (Place) placeWithDist[0];
      Double distGap = (Double) placeWithDist[1];

      brief.setId(place.getId());
      brief.setName(place.getName());
      brief.setCategory(place.getCategory());
      brief.setAddress(place.getAddress());
      brief.setThumbnail(place.getThumbnail());
      brief.setTagSummary(placesTagCounts.get(place.getId()));
      brief.setDistance(distGap);
      return brief;
    }).toList();
  }
}
