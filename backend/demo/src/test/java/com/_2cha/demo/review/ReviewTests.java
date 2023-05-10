package com._2cha.demo.review;


import static org.assertj.core.api.Assertions.assertThat;

import com._2cha.demo.global.infra.storage.service.FileStorageService;
import com._2cha.demo.member.domain.Member;
import com._2cha.demo.place.domain.Category;
import com._2cha.demo.place.domain.Place;
import com._2cha.demo.review.controller.ReviewController;
import com._2cha.demo.review.domain.Tag;
import com._2cha.demo.review.dto.ReviewResponse;
import com._2cha.demo.review.dto.WriteReviewRequest;
import jakarta.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
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
class ReviewTests {

  @Autowired
  ReviewController reviewController;
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

    em.persist(Tag.createTag("이야기 나누기 좋아요", "🗣️"));
    em.persist(Tag.createTag("책 읽기 좋아요", "📖️"));
    em.persist(Member.createMember("member1@2cha.com", "1234", "member1"));
    em.persist(Member.createMember("member2@2cha.com", "1234", "member2"));
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

    Long[] tagIds = {1L};
    String[] imgs = {baseUrl + "images/abc.png", baseUrl + "images/def.png"};

    WriteReviewRequest request = new WriteReviewRequest();
    request.setTagIds(Arrays.stream(tagIds)
                            .toList());
    request.setImgUrls(Arrays.stream(imgs)
                             .toList());

    reviewController.writePlaceReview(1L, 1L, request);
  }


  @Test
  void getPlaceReviews() {
    List<ReviewResponse> place1Reviews = reviewController.getPlaceReviews(1L);
    assertThat(place1Reviews).extracting("member")
                             .extracting("name")
                             .containsExactly("member1");

    List<ReviewResponse> place2Reviews = reviewController.getPlaceReviews(2L);
    assertThat(place2Reviews).isEmpty();
  }

  @Test
  void getMemberReviews() {

    List<ReviewResponse> member1Reviews = reviewController.getMemberReviews(1L,
                                                                            PageRequest.of(0, 1));
    assertThat(member1Reviews).extracting("place")
                              .extracting("name")
                              .containsExactly("히든아워");

    List<ReviewResponse> member2Reviews = reviewController.getMemberReviews(2L,
                                                                            PageRequest.of(0, 1));
    assertThat(member2Reviews).isEmpty();
  }

  @Test
  void deleteReview() {
    List<ReviewResponse> memberReviews;
    List<ReviewResponse> placeReviews = reviewController.getPlaceReviews(1L);

    assertThat(placeReviews).extracting("member")
                            .extracting("name")
                            .containsExactly("member1");

    reviewController.deleteReview(1L, 1L);
    placeReviews = reviewController.getPlaceReviews(1L);
    assertThat(placeReviews).isEmpty();

    memberReviews = reviewController.getMemberReviews(1L, PageRequest.of(0, 1));
    assertThat(memberReviews).isEmpty();
  }
}