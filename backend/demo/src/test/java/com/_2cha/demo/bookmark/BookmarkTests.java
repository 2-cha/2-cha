package com._2cha.demo.bookmark;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com._2cha.demo.bookmark.controller.BookmarkController;
import com._2cha.demo.bookmark.domain.ItemType;
import com._2cha.demo.bookmark.dto.BookmarkBriefResponse;
import com._2cha.demo.bookmark.dto.BookmarkCreateRequest;
import com._2cha.demo.bookmark.dto.BookmarkRemovedResponse;
import com._2cha.demo.bookmark.dto.PlaceBookmarkResponse;
import com._2cha.demo.bookmark.dto.ReviewBookmarkResponse;
import com._2cha.demo.collection.service.CollectionService;
import com._2cha.demo.global.exception.ConflictException;
import com._2cha.demo.global.exception.ForbiddenException;
import com._2cha.demo.global.exception.NotFoundException;
import com._2cha.demo.member.domain.Member;
import com._2cha.demo.place.domain.Category;
import com._2cha.demo.place.domain.Place;
import com._2cha.demo.review.domain.Tag;
import com._2cha.demo.review.service.ReviewService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.validation.Validator;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles({"test"})
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
public class BookmarkTests {

  @Autowired
  EntityManager em;
  @Autowired
  Validator validator;
  @Autowired
  CollectionService collectionService;
  @Autowired
  ReviewService reviewService;
  @Autowired
  BookmarkController bookmarkController;

  JPAQueryFactory queryFactory;

  @BeforeEach
  void mockUp() {
    this.queryFactory = new JPAQueryFactory(em);
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
    em.persist(Member.createMember("member2@2cha.com", "1234", "member2"));
    em.persist(Tag.createTag("이야기 나누기 좋아요", "🗣️"));

    Long[] tagIds = {1L};
    String[] imgs = {"abc", "def"};
    // member1 review
    reviewService.writeReview(1L, 2L, Arrays.stream(tagIds).toList(), Arrays.stream(imgs).toList());
    reviewService.writeReview(1L, 3L, Arrays.stream(tagIds).toList(), Arrays.stream(imgs).toList());
    reviewService.writeReview(1L, 4L, Arrays.stream(tagIds).toList(), Arrays.stream(imgs).toList());
    // member2 review
    reviewService.writeReview(2L, 2L, Arrays.stream(tagIds).toList(), Arrays.stream(imgs).toList());

    Long[] reviewIds = {1L, 3L};

    collectionService.createCollection(1L, "Member1 Collection", "My first collection",
                                       "https://dummy.img",
                                       Arrays.stream(reviewIds).toList());
  }

  @Test
  void createReviewBookmarkTest() {

    sendCreateRequest(ItemType.REVIEW, 1L, 1L);
    List<BookmarkBriefResponse> reviewBookmarks = bookmarkController.getReviewBookmarkList(1L);
    assertThat(reviewBookmarks).hasSize(1);
    assertThat(reviewBookmarks).extracting("thumbnail")
                               .containsExactly("abc"); // review#1 first image
  }

  @Test
  void createReviewBookmarkFail() {

    List<BookmarkBriefResponse> reviewBookmarks;
    // invalid review id
    assertThatThrownBy(() -> sendCreateRequest(ItemType.REVIEW, 1L, 10L))
        .isInstanceOf(NotFoundException.class);

    // already bookmarked review
    sendCreateRequest(ItemType.REVIEW, 1L, 1L);
    reviewBookmarks = bookmarkController.getReviewBookmarkList(1L);
    assertThat(reviewBookmarks).hasSize(1);
    assertThatThrownBy(() -> sendCreateRequest(ItemType.REVIEW, 1L, 1L))
        .isInstanceOf(ConflictException.class);
    sendCreateRequest(ItemType.PLACE, 1L, 1L); // OK: same item id but different type
  }

  @Test
  void createCollectionBookmarkTest() {

    sendCreateRequest(ItemType.COLLECTION, 1L, 1L);
    List<BookmarkBriefResponse> collectionBookmarks = bookmarkController.getCollectionBookmarkList(
        1L);
    assertThat(collectionBookmarks).hasSize(1);
    assertThat(collectionBookmarks).extracting("thumbnail")
                                   .containsExactly("https://dummy.img"); // collection#1 thumbnail
  }

  @Test
  void createCollectionBookmarkFail() {

    // invalid collection id
    assertThatThrownBy(() -> sendCreateRequest(ItemType.COLLECTION, 1L, 10L))
        .isInstanceOf(NotFoundException.class);

    // private collection id
    collectionService.updateCollection(1L, 1L, null, null, null, false);
    assertThat(collectionService.getMemberCollections(1L, true)).isEmpty();
    assertThatThrownBy(() -> sendCreateRequest(ItemType.COLLECTION, 1L, 1L))
        .isInstanceOf(NotFoundException.class);
    collectionService.updateCollection(1L, 1L, null, null, null, true);

    // already bookmarked collection
    sendCreateRequest(ItemType.COLLECTION, 1L, 1L);
    assertThat(bookmarkController.getCollectionBookmarkList(1L)).hasSize(1);
    assertThatThrownBy(() -> sendCreateRequest(ItemType.COLLECTION, 1L, 1L))
        .isInstanceOf(ConflictException.class);
    sendCreateRequest(ItemType.REVIEW, 1L, 1L); // OK: same item id but different type
  }

  @Test
  void createPlaceBookmarkTest() {

    sendCreateRequest(ItemType.PLACE, 1L, 1L);
    List<BookmarkBriefResponse> placeBookmarks = bookmarkController.getPlaceBookmarkList(1L);
    assertThat(placeBookmarks).hasSize(1);
    assertThat(placeBookmarks).extracting("thumbnail")
                              .containsExactly(// place#1 thumbnail
                                               "https://img1.kakaocdn.net/cthumb/local/R0x420/?fname=http%3A%2F%2Ft1.daumcdn.net%2Fplace%2F0732DF7C4D1D4631B167658FF31836B0,hnet/cthumb/local/R0x420/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flocal%2FkakaomapPhoto%2Freview%2Fa910cd15332fa03d0924070d52f60092479bfa87%3Foriginal,https://img1.kakaocdn.net/cthumb/local/R0x420/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flocal%2FkakaomapPhoto%2Freview%2F969c680aae8906d44bcd5e734b32b8c1d778ff8e%3Foriginal,https://img1.kakaocdn.net/cthumb/local/R0x420/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flocal%2FkakaomapPhoto%2Freview%2F54660b5f751cbea3ec29cea8f80da0bd941c2e11%3Foriginal,https://img1.kakaocdn.net/cthumb/local/R0x420/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flocal%2FkakaomapPhoto%2Freview%2Fdf0d59883daabea5fe12f3fcbf99e2bef316e4a5%3Foriginal");
  }

  @Test
  void createPlaceBookmarkFail() {
    // invalid place id
    assertThatThrownBy(() -> sendCreateRequest(ItemType.PLACE, 1L, 10L))
        .isInstanceOf(NotFoundException.class);

    // already bookmarked place
    sendCreateRequest(ItemType.PLACE, 1L, 1L);
    assertThat(bookmarkController.getPlaceBookmarkList(1L)).hasSize(1);
    assertThatThrownBy(() -> sendCreateRequest(ItemType.PLACE, 1L, 1L))
        .isInstanceOf(ConflictException.class);
    sendCreateRequest(ItemType.COLLECTION, 1L, 1L); // OK: same item id but different type
  }

  @Test
  void removeBookmarkTest() {
    sendCreateRequest(ItemType.REVIEW, 1L, 1L);
    List<BookmarkBriefResponse> reviewBookmarks = bookmarkController.getReviewBookmarkList(1L);
    assertThat(reviewBookmarks).hasSize(1);
    assertThat(reviewBookmarks).extracting("thumbnail")
                               .containsExactly("abc"); // review#1 first image

    sendRemoveRequest(1L, 1L);
    reviewBookmarks = bookmarkController.getReviewBookmarkList(1L);
    assertThat(reviewBookmarks).isEmpty();
  }


  @Test
  void removeBookmarkFail() {
    sendCreateRequest(ItemType.REVIEW, 1L, 1L);
    List<BookmarkBriefResponse> reviewBookmarks = bookmarkController.getReviewBookmarkList(1L);
    assertThat(reviewBookmarks).hasSize(1);

    // invalid bookmark id
    assertThatThrownBy(() -> sendRemoveRequest(1L, 19L)).isInstanceOf(NotFoundException.class);
    reviewBookmarks = bookmarkController.getReviewBookmarkList(1L);
    assertThat(reviewBookmarks).hasSize(1);

    // other member's bookmark
    assertThatThrownBy(() -> sendRemoveRequest(2L, 1L)).isInstanceOf(ForbiddenException.class);
    reviewBookmarks = bookmarkController.getReviewBookmarkList(1L);
    assertThat(reviewBookmarks).hasSize(1);
  }

  @Test
  void getReviewBookmarkDetailTest() {
    sendCreateRequest(ItemType.REVIEW, 1L, 1L);
    ReviewBookmarkResponse reviewBookmark = bookmarkController.getReviewBookmark(1L, 1L);
    assertThat(reviewBookmark).extracting("id").isEqualTo(1L);
    assertThat(reviewBookmark).extracting("review")
                              .extracting("id")
                              .isEqualTo(1L);
  }

  @Test
  void getPlaceBookmarkDetailTest() {
    sendCreateRequest(ItemType.PLACE, 1L, 1L);
    PlaceBookmarkResponse placeBookmark = bookmarkController.getPlaceBookmark(1L, 1L);
    assertThat(placeBookmark).extracting("id").isEqualTo(1L);
    assertThat(placeBookmark).extracting("place")
                             .extracting("name")
                             .isEqualTo("히든아워");
  }

  @Test
  void getBookmarkDetailFail() {
    sendCreateRequest(ItemType.REVIEW, 1L, 1L);
    sendCreateRequest(ItemType.PLACE, 1L, 1L);
    assertThat(bookmarkController.getReviewBookmarkList(1L)).extracting("id").containsExactly(1L);
    assertThat(bookmarkController.getPlaceBookmarkList(1L)).extracting("id").containsExactly(2L);

    // invalid bookmark id
    assertThatThrownBy(() -> bookmarkController.getReviewBookmark(1L, 10L)).isInstanceOf(
        NotFoundException.class);
    assertThatThrownBy(() -> bookmarkController.getPlaceBookmark(1L, 10L)).isInstanceOf(
        NotFoundException.class);

    // other member's bookmark
    assertThatThrownBy(() -> bookmarkController.getReviewBookmark(2L, 1L)).isInstanceOf(
        ForbiddenException.class);
    assertThatThrownBy(() -> bookmarkController.getPlaceBookmark(2L, 2L)).isInstanceOf(
        ForbiddenException.class);
  }


  private void sendCreateRequest(ItemType type, Long memberId, Long itemId) {
    BookmarkCreateRequest request = new BookmarkCreateRequest();
    request.setItemId(itemId);
    bookmarkController.createBookmark(memberId, type, request);
  }

  private BookmarkRemovedResponse sendRemoveRequest(Long memberId, Long bookmarkId) {
    return bookmarkController.removeBookmark(memberId, bookmarkId);
  }
}
