package com._2cha.demo.place.service;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.stream.Collectors.toMap;

import com._2cha.demo.bookmark.dto.BookmarkCountProjection;
import com._2cha.demo.bookmark.dto.BookmarkStatus;
import com._2cha.demo.bookmark.exception.AlreadyBookmarkedException;
import com._2cha.demo.bookmark.exception.NotBookmarkedException;
import com._2cha.demo.global.event.FirstReviewCreatedEvent;
import com._2cha.demo.global.infra.imageupload.service.ImageUploadService;
import com._2cha.demo.global.infra.storage.service.FileStorageService;
import com._2cha.demo.member.domain.Member;
import com._2cha.demo.member.exception.NoSuchMemberException;
import com._2cha.demo.member.service.MemberService;
import com._2cha.demo.place.domain.Category;
import com._2cha.demo.place.domain.Place;
import com._2cha.demo.place.domain.PlaceBookmark;
import com._2cha.demo.place.dto.FilterBy;
import com._2cha.demo.place.dto.NearbyPlaceSearchParams;
import com._2cha.demo.place.dto.PlaceBriefResponse;
import com._2cha.demo.place.dto.PlaceBriefWithDistanceResponse;
import com._2cha.demo.place.dto.PlaceCreatedResponse;
import com._2cha.demo.place.dto.PlaceDetailResponse;
import com._2cha.demo.place.dto.PlaceSearchResponse;
import com._2cha.demo.place.dto.PlaceSuggestionResponse;
import com._2cha.demo.place.dto.SortBy;
import com._2cha.demo.place.dto.SortOrder;
import com._2cha.demo.place.exception.InvalidTagCountSortException;
import com._2cha.demo.place.exception.NoSuchPlaceException;
import com._2cha.demo.place.repository.PlaceBookmarkRepository;
import com._2cha.demo.place.repository.PlaceQueryRepository;
import com._2cha.demo.place.repository.PlaceRepository;
import com._2cha.demo.review.dto.TagCountResponse;
import com._2cha.demo.review.service.ReviewService;
import com._2cha.demo.util.GeomUtils;
import com._2cha.demo.util.HangulUtils;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceService {

  private final PlaceRepository placeRepository;
  private final PlaceQueryRepository placeQueryRepository;
  private final PlaceBookmarkRepository placeBookmarkRepository;
  private final FileStorageService fileStorageService;
  private final ImageUploadService imageUploadService;
  private final MemberService memberService;
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
                                          Double lon, Double lat, String site) {

    Place place = Place.createPlace(name, category, address, lotAddress, lon, lat,
                                    null, null, site);
    placeRepository.save(place);
    return new PlaceCreatedResponse(place, fileStorageService.getBaseUrl());
  }

  @Transactional
  @Async("placeCommandTaskExecutor")
  @EventListener(FirstReviewCreatedEvent.class)
  public void updateImageAsync(FirstReviewCreatedEvent event) {
    Place place = placeRepository.findById(event.getPlaceId());
    if (place == null) return; //TODO: handle it
    if (place.getImageUrlPath() != null) return;

    place.updateImage(event.getImageUrlPath(), event.getThumbUrlPath());
    placeRepository.save(place);
  }

  @Transactional
  public void createBookmark(Long memberId, Long placeId) {
    Member member = memberService.findById(memberId);
    if (member == null) throw new NoSuchMemberException();

    Place place = placeRepository.findById(placeId);
    if (place == null) throw new NoSuchPlaceException();

    if (placeBookmarkRepository.findByMemberIdAndPlaceId(memberId, placeId) != null) {
      throw new AlreadyBookmarkedException();
    }

    PlaceBookmark bookmark = new PlaceBookmark(member, place);
    placeBookmarkRepository.save(bookmark);
  }

  @Transactional
  public void removeBookmark(Long memberId, Long placeId) {
    PlaceBookmark bookmark = placeBookmarkRepository.findByMemberIdAndPlaceId(memberId, placeId);
    if (bookmark == null) {
      throw new NotBookmarkedException();
    }

    placeBookmarkRepository.delete(bookmark);
  }

  /*-----------
   @ Queries
   ----------*/
  public Place findPlaceById(Long id) {
    Place place = placeRepository.findById(id);
    if (place == null) throw new NoSuchPlaceException();

    return place;
  }

  public PlaceBriefWithDistanceResponse getPlaceBriefWithDistance(Long placeId, Double lat,
                                                                  Double lon, Integer summarySize) {
    Point cur = GeomUtils.createPoint(lat, lon);
    PlaceBriefWithDistanceResponse brief = placeQueryRepository.getPlaceBriefWithDistance(placeId,
                                                                                          cur,
                                                                                          fileStorageService.getBaseUrl());
    if (brief == null) throw new NoSuchPlaceException();

    brief.setTagSummary(reviewService.getReviewTagCountByPlaceId(placeId, summarySize));

    return brief;
  }


  public PlaceBriefResponse getPlaceBriefById(Long placeId, Integer summarySize) {

    PlaceBriefResponse brief = placeQueryRepository.getPlaceBriefById(placeId,
                                                                      fileStorageService.getBaseUrl());
    if (brief == null) throw new NoSuchPlaceException();

    brief.setTagSummary(reviewService.getReviewTagCountByPlaceId(placeId, summarySize));

    return brief;
  }

  public List<PlaceBriefResponse> getPlacesBriefByIdIn(List<Long> placeIds, Integer summarySize) {
    List<PlaceBriefResponse> briefs = placeQueryRepository.getPlacesBriefsByIdIn(placeIds,
                                                                                 fileStorageService.getBaseUrl());
    if (briefs.isEmpty()) return briefs;

    Map<Long, List<TagCountResponse>> placesTagCounts = reviewService.getReviewTagCountsByPlaceIdIn(
        briefs.stream().map(place -> place.getId()).toList(), summarySize);

    briefs.forEach(brief -> brief.setTagSummary(placesTagCounts.get(brief.getId())));

    return briefs;
  }

  public PlaceDetailResponse getPlaceDetailById(Long id) {
    PlaceDetailResponse detail = placeQueryRepository.getPlaceDetailById(id,
                                                                         fileStorageService.getBaseUrl());
    if (detail == null) throw new NoSuchPlaceException();

    detail.setTags(reviewService.getReviewTagCountByPlaceId(id, null));

    return detail;
  }

  @Async("placeQueryTaskExecutor")
  public CompletableFuture<List<PlaceSuggestionResponse>> suggestNearbyPlacesAsync(Double lat,
                                                                                   Double lon) {

    List<Pair<Place, Double>> placesWithDist =
        placeQueryRepository.findAround(new NearbyPlaceSearchParams(lat, lon,
                                                                    SUGGESTION_MAX_DIST,
                                                                    FilterBy.DEFAULT, null,
                                                                    SortBy.DISTANCE, SortOrder.ASC,
                                                                    0L, SUGGESTION_SIZE));

    return completedFuture(placesWithDist.stream()
                                         .map(pair -> new PlaceSuggestionResponse(
                                             pair.getFirst(), pair.getSecond()))
                                         .toList());
  }

  public List<PlaceBriefWithDistanceResponse>
  searchPlacesWithFilterAndSorting(NearbyPlaceSearchParams searchParams) {
    if (searchParams.getSortBy() == SortBy.TAG_COUNT
        && searchParams.getFilterBy() != FilterBy.TAG) {throw new InvalidTagCountSortException();}

    List<Pair<Place, Double>> placesWithDist = placeQueryRepository.findAround(searchParams);
    if (placesWithDist.isEmpty()) return Collections.emptyList();

    Map<Long, List<TagCountResponse>> placesTagCounts = reviewService.getReviewTagCountsByPlaceIdIn(
        placesWithDist.stream().map(pair -> pair.getFirst().getId()).toList(), REVIEW_SUMMARY_SIZE);

    return placesWithDist.stream().map(pair -> {
      Place place = pair.getFirst();
      Double distGap = pair.getSecond();
      PlaceBriefWithDistanceResponse brief = new PlaceBriefWithDistanceResponse(place, distGap,
                                                                                fileStorageService.getBaseUrl());
      brief.setTagSummary(placesTagCounts.get(place.getId()));
      return brief;
    }).toList();
  }

  public List<PlaceBriefResponse> getBookmarkedPlaces(Long memberId) {
    List<PlaceBookmark> bookmarks = placeBookmarkRepository.findAllByMemberId(memberId);
    List<Long> placeIds = bookmarks.stream().map(b -> b.getPlace().getId()).toList();
    return getPlacesBriefByIdIn(placeIds, REVIEW_SUMMARY_SIZE);
  }

  public void setResponseBookmarkStatus(Long memberId, List<? extends PlaceBriefResponse> places) {
    List<Long> placeIds = places.stream().map(PlaceBriefResponse::getId).toList();
    List<PlaceBookmark> bookmarks = placeBookmarkRepository.findAllByMemberIdAndPlaceIdIn(
        memberId, placeIds);
    List<Long> bookmarkedIds = bookmarks.stream().map(b -> b.getPlace().getId()).toList();
    Map<Long, Long> totalCountMap = placeBookmarkRepository.countAllByPlaceIdIn(placeIds)
                                                           .stream()
                                                           .collect(
                                                               toMap(
                                                                   BookmarkCountProjection::getId,
                                                                   BookmarkCountProjection::getCount));

    for (var place : places) {
      place.setBookmarkStatus(
          new BookmarkStatus(bookmarkedIds.contains(place.getId()),
                             totalCountMap.getOrDefault(place.getId(), 0L)));
    }
  }

  public void setResponseBookmarkStatus(Long memberId, PlaceDetailResponse place) {
    PlaceBookmark bookmark = placeBookmarkRepository.findByMemberIdAndPlaceId(memberId,
                                                                              place.getId());
    Long count = placeBookmarkRepository.countAllByPlaceId(place.getId());
    place.setBookmarkStatus(new BookmarkStatus(bookmark != null, count));
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

  public List<PlaceBriefWithDistanceResponse> getPlacesBriefWithDistance(
      NearbyPlaceSearchParams nearbyPlacesParams) {
    List<Pair<Place, Double>> placesWithDist = placeQueryRepository.findAround(nearbyPlacesParams);
    if (placesWithDist.isEmpty()) return Collections.emptyList();

    Map<Long, List<TagCountResponse>> placesTagCounts = reviewService.getReviewTagCountsByPlaceIdIn(
        placesWithDist.stream().map(pair -> pair.getFirst().getId()).toList(), REVIEW_SUMMARY_SIZE);

    return placesWithDist.stream().map(pair -> {
      Place place = pair.getFirst();
      Double distGap = pair.getSecond();
      PlaceBriefWithDistanceResponse brief = new PlaceBriefWithDistanceResponse(place, distGap,
                                                                                fileStorageService.getBaseUrl());
      brief.setTagSummary(placesTagCounts.get(place.getId()));
      return brief;
    }).toList();
  }
}
