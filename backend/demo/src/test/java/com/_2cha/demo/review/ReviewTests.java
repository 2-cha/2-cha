package com._2cha.demo.review;


import static org.assertj.core.api.Assertions.assertThat;

import com._2cha.demo.member.domain.Member;
import com._2cha.demo.place.domain.Category;
import com._2cha.demo.place.domain.Place;
import com._2cha.demo.review.controller.ReviewController;
import com._2cha.demo.review.domain.Tag;
import com._2cha.demo.review.dto.MemberReviewResponse;
import com._2cha.demo.review.dto.PlaceReviewResponse;
import com._2cha.demo.review.dto.WriteReviewRequest;
import jakarta.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
  EntityManager em;

  @BeforeEach
  @Transactional
  void mockUp() {
    em.persist(Tag.createTag("이야기 나누기 좋아요", "🗣️"));
    em.persist(Tag.createTag("책 읽기 좋아요", "📖️"));
    em.persist(Member.createMember("member1@2cha.com", "1234", "member1"));
    em.persist(Member.createMember("member2@2cha.com", "1234", "member2"));
    em.persist(Place.createPlace("히든아워", Category.WINE_BAR, "서울 종로구 종로32길 21 4층",
                                 127.001312694, 37.570090435,
                                 "https://img1.kakaocdn.net/cthumb/local/R0x420/?fname=http%3A%2F%2Ft1.daumcdn.net%2Fplace%2F0732DF7C4D1D4631B167658FF31836B0,hnet/cthumb/local/R0x420/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flocal%2FkakaomapPhoto%2Freview%2Fa910cd15332fa03d0924070d52f60092479bfa87%3Foriginal,https://img1.kakaocdn.net/cthumb/local/R0x420/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flocal%2FkakaomapPhoto%2Freview%2F969c680aae8906d44bcd5e734b32b8c1d778ff8e%3Foriginal,https://img1.kakaocdn.net/cthumb/local/R0x420/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flocal%2FkakaomapPhoto%2Freview%2F54660b5f751cbea3ec29cea8f80da0bd941c2e11%3Foriginal,https://img1.kakaocdn.net/cthumb/local/R0x420/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flocal%2FkakaomapPhoto%2Freview%2Fdf0d59883daabea5fe12f3fcbf99e2bef316e4a5%3Foriginal"));
    em.persist(Place.createPlace("flux", Category.WINE_BAR, "서울 종로구 자하문로7길 24",
                                 126.970755522, 37.578890929,
                                 ""));

    Long[] ids = {1L};
    String[] imgs = {"abc", "def"};
    WriteReviewRequest request = new WriteReviewRequest();
    request.setTagIds(Arrays.stream(ids)
                            .toList());
    request.setImgUrls(Arrays.stream(imgs)
                             .toList());

    reviewController.writePlaceReview(1L, 1L, request);
  }


  @Test
  void getPlaceReviews() {
    List<PlaceReviewResponse> place1Reviews = reviewController.getPlaceReviews(1L);
    assertThat(place1Reviews).extracting("member")
                             .extracting("name")
                             .containsExactly("member1");

    List<PlaceReviewResponse> place2Reviews = reviewController.getPlaceReviews(2L);
    assertThat(place2Reviews).isEmpty();
  }

  @Test
  void getMemberReviews() {
    List<MemberReviewResponse> member1Reviews = reviewController.getMemberReviews(1L);
    assertThat(member1Reviews).extracting("place")
                              .extracting("name")
                              .containsExactly("히든아워");

    List<MemberReviewResponse> member2Reviews = reviewController.getMemberReviews(2L);
    assertThat(member2Reviews).isEmpty();
  }

  @Test
  void deleteReview() {
    List<MemberReviewResponse> memberReviews;
    List<PlaceReviewResponse> placeReviews = reviewController.getPlaceReviews(1L);

    assertThat(placeReviews).extracting("member")
                            .extracting("name")
                            .containsExactly("member1");

    reviewController.deleteReview(1L, 1L);
    placeReviews = reviewController.getPlaceReviews(1L);
    assertThat(placeReviews).isEmpty();

    memberReviews = reviewController.getMemberReviews(1L);
    assertThat(memberReviews).isEmpty();
  }
}