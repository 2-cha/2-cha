package com._2cha.demo.place;

import static com._2cha.demo.review.domain.Category.ACTIVITY;

import com._2cha.demo.global.infra.storage.service.FileStorageService;
import com._2cha.demo.member.domain.Member;
import com._2cha.demo.member.service.MemberService;
import com._2cha.demo.place.controller.PlaceController;
import com._2cha.demo.place.domain.Category;
import com._2cha.demo.place.domain.Place;
import com._2cha.demo.place.dto.FilterBy;
import com._2cha.demo.place.dto.PlaceBriefWithDistanceResponse;
import com._2cha.demo.place.dto.PlaceDetailResponse;
import com._2cha.demo.place.dto.SortBy;
import com._2cha.demo.review.domain.Tag;
import com._2cha.demo.review.service.ReviewService;
import jakarta.persistence.EntityManager;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles({"test"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PlaceTests {

  @Autowired
  PlaceController placeController;
  @Autowired
  ReviewService reviewService;
  @Autowired
  MemberService memberService;
  @Autowired
  FileStorageService fileStorageService;

  @Autowired
  EntityManager em;

  @BeforeEach
  @Transactional
  void mockUp() {
    String baseUrl = fileStorageService.getBaseUrl();

    em.createNativeQuery(
          "CREATE ALIAS IF NOT EXISTS H2GIS_SPATIAL FOR \"org.h2gis.functions.factory.H2GISFunctions.load\";\n"
          + "CALL H2GIS_SPATIAL();")
      .executeUpdate();

    em.persist(Place.createPlace("히든아워", Category.WINE_BAR,
                                 "서울 종로구 종로32길 21 4층",
                                 "(지번) 종로5가 395-8",
                                 127.001312694, 37.570090435,
                                 "images/1.png",
                                 "images/thumb_1.png",
                                 "https://instagram.com/hidden_hour"));
    em.persist(Place.createPlace("flux", Category.WINE_BAR,
                                 "서울 종로구 자하문로7길 24",
                                 "(지번) 통인동 149-5",
                                 126.970755522, 37.578890929,
                                 "images/2.png",
                                 "images/thumb_2.png",
                                 ""));
    em.persist(Place.createPlace("standbyme", Category.WINE_BAR,
                                 "서울 종로구 자하문로13길 4-7",
                                 "(지번) 통인동 102",
                                 126.97077385, 37.58036915,
                                 "images/3.png",
                                 "images/thumb_3.png",
                                 "https://www.instagram.com/standbyme_seoul"));
    em.persist(Place.createPlace("M바", Category.COCKTAIL_BAR,
                                 "서울 종로구 종로8길 16",
                                 "(지번) 관철동 44-3",
                                 126.98386315, 37.569345648,
                                 "images/4.png",
                                 "images/thumb_4.png",
                                 ""));
    em.persist(Place.createPlace("NYIL", Category.COCKTAIL_BAR,
                                 "서울 종로구 사직로 99",
                                 "(지번) 필운동 225-2",
                                 126.969806904, 37.576275315,
                                 "images/5.png",
                                 "images/thumb_5.png",
                                 "https://instagram.com/nyil_seoul"));
    em.persist(Place.createPlace("SAM1999 종로점", Category.COCKTAIL_BAR,
                                 "서울 종로구 종로12길 6-20 2층",
                                 "(지번) 관철동 16-9",
                                 126.984614536, 37.569841204,
                                 "images/6.png",
                                 "images/thumb_6.png",
                                 ""));

    em.persist(Member.createMember("member1@2cha.com", "1234", "member1"));
    em.persist(Member.createMember("member2@2cha.com", "1234", "member2"));
    em.persist(Tag.createTag("이야기 나누기 좋아요", "🗣️", ACTIVITY));

    Long[] tagIds = {1L};
    String[] imgs = {baseUrl + "images/abc.png", baseUrl + "images/def.png"};
    // member1 review
    reviewService.writeReview(1L, 2L, Arrays.stream(tagIds).toList(), Arrays.stream(imgs).toList());
  }


  @Test
  void getPlaceDetailById() {

    PlaceDetailResponse place = placeController.getPlaceDetailById(3L);
    Assertions.assertThat(place.getName())
              .isEqualTo("standbyme");
    Assertions.assertThat(place.getTags()).isEmpty();

    PlaceDetailResponse placeWithReview = placeController.getPlaceDetailById(2L);
    Assertions.assertThat(placeWithReview.getTags())
              .extracting("message")
              .containsExactly("이야기 나누기 좋아요");
    Assertions.assertThat(placeWithReview.getTags())
              .extracting("count")
              .containsExactly(1);
  }

  @Test
  void getNearbyPlace_Default() {
    Map<String, Object> query = new HashMap<>();
    // 종로
    Double maxDist = 5000.0;
    Double lat = 37.5957;
    Double lon = 126.9803;

    List<PlaceBriefWithDistanceResponse> places =
        placeController.getNearbyPlace(FilterBy.DEFAULT, null,
                                       SortBy.DISTANCE,
                                       maxDist,
                                       lat,
                                       lon,
                                       PageRequest.of(0, 5)
                                      );
    Assertions.assertThat(places).hasSize(5);

    List<PlaceBriefWithDistanceResponse> sorted = places.stream()
                                                        .sorted(
                                                            Comparator.comparing(
                                                                PlaceBriefWithDistanceResponse::getDistance))
                                                        .toList();
    Assertions.assertThat(places).isEqualTo(sorted);
  }
}