package com._2cha.demo.place;

import com._2cha.demo.member.domain.Member;
import com._2cha.demo.member.service.MemberService;
import com._2cha.demo.place.controller.PlaceController;
import com._2cha.demo.place.domain.Category;
import com._2cha.demo.place.domain.Place;
import com._2cha.demo.place.dto.FilterBy;
import com._2cha.demo.place.dto.NearbyPlaceRequest;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles({"test"})
class PlaceTests {

  @Autowired
  PlaceController placeController;
  @Autowired
  ReviewService reviewService;
  @Autowired
  MemberService memberService;

  @Autowired
  EntityManager em;

  @BeforeEach
  @Transactional
  void mockUp() {
    em.createNativeQuery(
          "CREATE ALIAS IF NOT EXISTS H2GIS_SPATIAL FOR \"org.h2gis.functions.factory.H2GISFunctions.load\";\n"
          + "CALL H2GIS_SPATIAL();")
      .executeUpdate();

    em.persist(Place.createPlace("히든아워", Category.WINE_BAR, "서울 종로구 종로32길 21 4층",
                                 127.001312694, 37.570090435,
                                 "https://img1.kakaocdn.net/cthumb/local/R0x420/?fname=http%3A%2F%2Ft1.daumcdn.net%2Fplace%2F0732DF7C4D1D4631B167658FF31836B0,hnet/cthumb/local/R0x420/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flocal%2FkakaomapPhoto%2Freview%2Fa910cd15332fa03d0924070d52f60092479bfa87%3Foriginal,https://img1.kakaocdn.net/cthumb/local/R0x420/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flocal%2FkakaomapPhoto%2Freview%2F969c680aae8906d44bcd5e734b32b8c1d778ff8e%3Foriginal,https://img1.kakaocdn.net/cthumb/local/R0x420/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flocal%2FkakaomapPhoto%2Freview%2F54660b5f751cbea3ec29cea8f80da0bd941c2e11%3Foriginal,https://img1.kakaocdn.net/cthumb/local/R0x420/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flocal%2FkakaomapPhoto%2Freview%2Fdf0d59883daabea5fe12f3fcbf99e2bef316e4a5%3Foriginal"));
    em.persist(Place.createPlace("flux", Category.WINE_BAR, "서울 종로구 자하문로7길 24",
                                 126.970755522, 37.578890929,
                                 ""));
    em.persist(Place.createPlace("standbyme", Category.WINE_BAR, "서울 종로구 자하문로13길 4-7",
                                 126.97077385, 37.58036915,
                                 "https://img1.kakaocdn.net/cthumb/local/R0x420/?fname=http%3A%2F%2Ft1.daumcdn.net%2Fplace%2FA925100FB8364B0CB3ECE1A3BC9F6akaocdn.net/cthumb/local/R0x420/?fname=https%3A%2F%2Fpostfiles.pstatic.net%2FMjAyMzAxMzBfMTUg%2FMDAxNjc1MDc4MzQxMDA0.Xx17cmFel8skPPQctFdg543RC9LE_loe4GpVsxDXxTsg.KsdLbij7P8ABrieJ-2H-XKUAUdiChqQ2vGC_ZoUC2LUg.JPEG.hj1231k%2FIMG_6131.JPG%3Ftype%3Dw966,https://img1.kakaocdn.net/cthumb/local/R0x420/?fname=https%3A%2F%2Fpostfiles.pstatic.net%2FMjAyMzAxMzBfMTkz%2FMDAxNjc1MDc4MzM1OTQ2.Kc6YzATkcDe7L6aNQauaUK7I0D8lSg0O4tD1hFrjRrsg.I1bDyFh1m2ncjk8VBmoPYZRDGWQdad5u7sBpXn6uDFMg.JPEG.hj1231k%2FIMG_6127.JPG%3Ftype%3Dw966,https://img1.kakaocdn.net/cthumb/local/R0x420/?fname=https%3A%2F%2Fpostfiles.pstatic.net%2FMjAyMzAxMzBfMjkz%2FMDAxNjc1MDc4MzQxNzM3.8HWXKqZD61prfNSOPcsaEjT0JpuKo3eQjp9MHZgv_wMg.1NOjD6qxLRccR4wJBfEVieBppgXHLIwJ3wSOLq9Tv-Yg.JPEG.hj1231k%2FIMG_6120.JPG%3Ftype%3Dw966,https://img1.kakaocdn.net/cthumb/local/R0x420/?fname=https%3A%2F%2Fpostfiles.pstatic.net%2FMjAyMzAxMzBfMzIg%2FMDAxNjc1MDc4MzQwOTM3.tEp7j6SFDj1xhM04a1n9Uko0GM9iiKeaO4TSvvJfr64g.pMH2b5dNRJNUn93zQNKsF5bHKMuMolGUG-cP_sVEMDQg.JPEG.hj1231k%2FIMG_6130.JPG%3Ftype%3Dw966"));
    em.persist(Place.createPlace("M바", Category.COCKTAIL_BAR, "서울 종로구 종로8길 16",
                                 126.98386315, 37.569345648,
                                 ""));
    em.persist(Place.createPlace("NYIL", Category.COCKTAIL_BAR, "서울 종로구 사직로 99",
                                 126.969806904, 37.576275315,
                                 "https://img1.kakaocdn.net/cthumb/local/R0x420/?fname=http%3A%2F%2Ft1.kakaocdn.net%2Fmystore%2F50929B8EDFBD4AF481F8B6CD2FE552EF,https://img1thumb/local/R0x420/?fname=http%3A%2F%2Ft1.kakaocdn.net%2Fmystore%2FF3D8813CD53646F49FFD30ECE941A8F1,https://img1.kakaocdn.net/cthumb/local/R0x420/?fname=http%3A%2F%2Ft1.kakaocdn.net%2Fmystore%2F1D3D0AC28D70484A9ED598D1B1B12582,https://img1.kakaocdn.net/cthumb/local/R0x420/?fname=http%3A%2F%2Ft1.kakaocdn.net%2Fmystore%2F4C064724B643453EA95E86468525C5B1,https://img1.kakaocdn.net/cthumb/local/R0x420/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flocal%2FkakaomapPhoto%2Freview%2F7312cdb2530970dd9f7ff6de3b7a7d3e9f10f931%3Foriginal"));
    em.persist(Place.createPlace("SAM1999 종로점", Category.COCKTAIL_BAR, "서울 종로구 종로12길 6-20 2층",
                                 126.984614536, 37.569841204,
                                 ""));

    em.persist(Member.createMember("member1@2cha.com", "1234", "member1"));
    em.persist(Tag.createTag("이야기 나누기 좋아요", "🗣️"));

    Long[] ids = {1L};
    String[] imgs = {"abc", "def"};
    reviewService.writeReview(1L, 2L, Arrays.stream(ids).toList(), Arrays.stream(imgs).toList());
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
    NearbyPlaceRequest request = new NearbyPlaceRequest();

    Map<String, Object> query = new HashMap<>();
    // 종로
    query.put("min_dist", 0.0);
    query.put("max_dist", 5000.0);
    query.put("lat", 37.5957);
    query.put("lon", 126.9803);

    List<PlaceBriefWithDistanceResponse> places =
        placeController.getNearbyPlace(FilterBy.DEFAULT, null,
                                       SortBy.DISTANCE,
                                       5,
                                       (Double) query.get("min_dist"),
                                       (Double) query.get("max_dist"),
                                       (Double) query.get("lat"),
                                       (Double) query.get("lon"),
                                       query
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