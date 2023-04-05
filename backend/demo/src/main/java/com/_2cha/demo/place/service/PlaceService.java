package com._2cha.demo.place.service;

import com._2cha.demo.global.exception.NotFoundException;
import com._2cha.demo.place.domain.Place;
import com._2cha.demo.place.dto.PlaceBriefResponse;
import com._2cha.demo.place.dto.PlaceBriefWithDistanceResponse;
import com._2cha.demo.place.dto.PlaceDetailResponse;
import com._2cha.demo.place.repository.PlaceRepository;
import com._2cha.demo.review.service.ReviewService;
import com._2cha.demo.util.GeomUtils;
import java.util.List;
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

  public List<PlaceBriefWithDistanceResponse> getNearbyPlace(Double lat, Double lon,
                                                             Double minDist, Double maxDist,
                                                             Integer pageSize) {

    List<Object[]> places = placeRepository.findAround(lat, lon, minDist, maxDist, pageSize);

    return places.stream().map((placeWithDistance) -> {
      PlaceBriefWithDistanceResponse brief = new PlaceBriefWithDistanceResponse();
      Place place = (Place) placeWithDistance[0];
      Double distGap = (Double) placeWithDistance[1];

      brief.setId(place.getId());
      brief.setName(place.getName());
      brief.setCategory(place.getCategory());
      brief.setAddress(place.getAddress());
      brief.setThumbnail(place.getThumbnail());
      brief.setDistance(distGap);
      return brief;
    }).toList();
  }
}
