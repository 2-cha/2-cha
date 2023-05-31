package com._2cha.demo.bookmark;

import jakarta.persistence.EntityManager;
import jakarta.validation.Validator;
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

  //TODO: modify tests
//  @Autowired
//  CollectionService collectionService;
//  @Autowired
//  ReviewService reviewService;
//
//  @Autowired
//  FileStorageService fileStorageService;
//
//  JPAQueryFactory queryFactory;
//
//  @BeforeEach
//  void mockUp() {
//    String baseUrl = fileStorageService.getBaseUrl();
//    this.queryFactory = new JPAQueryFactory(em);
//
//    em.createNativeQuery(
//          "CREATE ALIAS IF NOT EXISTS H2GIS_SPATIAL FOR \"org.h2gis.functions.factory.H2GISFunctions.load\";\n"
//          + "CALL H2GIS_SPATIAL();")
//      .executeUpdate();
//
//    em.persist(Place.createPlace("히든아워", Category.WINE_BAR,
//                                 "서울 종로구 종로32길 21 4층",
//                                 "(지번) 종로5가 395-8",
//                                 127.001312694, 37.570090435,
//                                 "images/1.png",
//                                 "images/thumb_1.png",
//                                 "https://instagram.com/hidden_hour"));
//    em.persist(Place.createPlace("flux", Category.WINE_BAR,
//                                 "서울 종로구 자하문로7길 24",
//                                 "(지번) 통인동 149-5",
//                                 126.970755522, 37.578890929,
//                                 "images/2.png",
//                                 "images/thumb_2.png",
//                                 ""));
//    em.persist(Place.createPlace("standbyme", Category.WINE_BAR,
//                                 "서울 종로구 자하문로13길 4-7",
//                                 "(지번) 통인동 102",
//                                 126.97077385, 37.58036915,
//                                 "images/3.png",
//                                 "images/thumb_3.png",
//                                 "https://www.instagram.com/standbyme_seoul"));
//    em.persist(Place.createPlace("M바", Category.COCKTAIL_BAR,
//                                 "서울 종로구 종로8길 16",
//                                 "(지번) 관철동 44-3",
//                                 126.98386315, 37.569345648,
//                                 "images/4.png",
//                                 "images/thumb_4.png",
//                                 ""));
//    em.persist(Place.createPlace("NYIL", Category.COCKTAIL_BAR,
//                                 "서울 종로구 사직로 99",
//                                 "(지번) 필운동 225-2",
//                                 126.969806904, 37.576275315,
//                                 "images/5.png",
//                                 "images/thumb_5.png",
//                                 "https://instagram.com/nyil_seoul"));
//    em.persist(Place.createPlace("SAM1999 종로점", Category.COCKTAIL_BAR,
//                                 "서울 종로구 종로12길 6-20 2층",
//                                 "(지번) 관철동 16-9",
//                                 126.984614536, 37.569841204,
//                                 "images/6.png",
//                                 "images/thumb_6.png",
//                                 ""));
//
//    em.persist(Member.createMember("member1@2cha.com", "1234", "member1"));
//    em.persist(Member.createMember("member2@2cha.com", "1234", "member2"));
//    em.persist(Tag.createTag("이야기 나누기 좋아요", "🗣️", ACTIVITY));
//
//    Long[] tagIds = {1L};
//    String[] imgs = {baseUrl + "images/abc.png", baseUrl + "images/def.png"};
//    // member1 review
//    reviewService.writeReview(1L, 2L, Arrays.stream(tagIds).toList(), Arrays.stream(imgs).toList());
//    reviewService.writeReview(1L, 3L, Arrays.stream(tagIds).toList(), Arrays.stream(imgs).toList());
//    reviewService.writeReview(1L, 4L, Arrays.stream(tagIds).toList(), Arrays.stream(imgs).toList());
//    // member2 review
//    reviewService.writeReview(2L, 2L, Arrays.stream(tagIds).toList(), Arrays.stream(imgs).toList());
//
//    Long[] reviewIds = {1L, 3L};
//
//    collectionService.createCollection(1L, "Member1 Collection", "My first collection",
//                                       baseUrl + "images/abc.png",
//                                       Arrays.stream(reviewIds).toList());
//  }
//
//  @Test
//  void createReviewBookmarkTest() {
//
//    sendCreateRequest(ItemType.REVIEW, 1L, 1L);
//    List<BookmarkBriefResponse> reviewBookmarks = bookmarkController.getReviewBookmarkList(1L);
//    assertThat(reviewBookmarks).hasSize(1);
//    assertThat(reviewBookmarks).extracting("thumbnail")
//                               .containsExactly(fileStorageService.getBaseUrl()
//                                                + "images/thumb_abc.png"); // review#1 first image's thumbnail
//  }
//
//  @Test
//  void createReviewBookmarkFail() {
//
//    List<BookmarkBriefResponse> reviewBookmarks;
//    // invalid review id
//    assertThatThrownBy(() -> sendCreateRequest(ItemType.REVIEW, 1L, 10L))
//        .isInstanceOf(NotFoundException.class);
//
//    // already bookmarked review
//    sendCreateRequest(ItemType.REVIEW, 1L, 1L);
//    reviewBookmarks = bookmarkController.getReviewBookmarkList(1L);
//    assertThat(reviewBookmarks).hasSize(1);
//    assertThatThrownBy(() -> sendCreateRequest(ItemType.REVIEW, 1L, 1L))
//        .isInstanceOf(ConflictException.class);
//    sendCreateRequest(ItemType.PLACE, 1L, 1L); // OK: same item id but different type
//  }
//
//  @Test
//  void createCollectionBookmarkTest() {
//
//    sendCreateRequest(ItemType.COLLECTION, 1L, 1L);
//    List<BookmarkBriefResponse> collectionBookmarks = bookmarkController.getCollectionBookmarkList(
//        1L);
//    assertThat(collectionBookmarks).hasSize(1);
//    assertThat(collectionBookmarks).extracting("thumbnail")
//                                   .containsExactly(fileStorageService.getBaseUrl()
//                                                    + "images/abc.png"); // collection#1 thumbnail
//  }
//
//  @Test
//  void createCollectionBookmarkFail() {
//
//    // invalid collection id
//    assertThatThrownBy(() -> sendCreateRequest(ItemType.COLLECTION, 1L, 10L))
//        .isInstanceOf(NotFoundException.class);
//
//    // private collection id
//    collectionService.updateCollection(1L, 1L, null, null, null, false);
//    assertThat(collectionService.getMemberCollections(1L, true)).isEmpty();
//    assertThatThrownBy(() -> sendCreateRequest(ItemType.COLLECTION, 1L, 1L))
//        .isInstanceOf(NotFoundException.class);
//    collectionService.updateCollection(1L, 1L, null, null, null, true);
//
//    // already bookmarked collection
//    sendCreateRequest(ItemType.COLLECTION, 1L, 1L);
//    assertThat(bookmarkController.getCollectionBookmarkList(1L)).hasSize(1);
//    assertThatThrownBy(() -> sendCreateRequest(ItemType.COLLECTION, 1L, 1L))
//        .isInstanceOf(ConflictException.class);
//    sendCreateRequest(ItemType.REVIEW, 1L, 1L); // OK: same item id but different type
//  }
//
//  @Test
//  void createPlaceBookmarkTest() {
//
//    sendCreateRequest(ItemType.PLACE, 1L, 1L);
//    List<BookmarkBriefResponse> placeBookmarks = bookmarkController.getPlaceBookmarkList(1L);
//    assertThat(placeBookmarks).hasSize(1);
//    assertThat(placeBookmarks).extracting("thumbnail")
//                              .containsExactly(// place#1 thumbnail
//                                               fileStorageService.getBaseUrl()
//                                               + "images/thumb_1.png");
//  }
//
//  @Test
//  void createPlaceBookmarkFail() {
//    // invalid place id
//    assertThatThrownBy(() -> sendCreateRequest(ItemType.PLACE, 1L, 10L))
//        .isInstanceOf(NotFoundException.class);
//
//    // already bookmarked place
//    sendCreateRequest(ItemType.PLACE, 1L, 1L);
//    assertThat(bookmarkController.getPlaceBookmarkList(1L)).hasSize(1);
//    assertThatThrownBy(() -> sendCreateRequest(ItemType.PLACE, 1L, 1L))
//        .isInstanceOf(ConflictException.class);
//    sendCreateRequest(ItemType.COLLECTION, 1L, 1L); // OK: same item id but different type
//  }
//
//  @Test
//  void removeBookmarkTest() {
//    sendCreateRequest(ItemType.REVIEW, 1L, 1L);
//    List<BookmarkBriefResponse> reviewBookmarks = bookmarkController.getReviewBookmarkList(1L);
//    assertThat(reviewBookmarks).hasSize(1);
//    assertThat(reviewBookmarks).extracting("thumbnail")
//                               .containsExactly(fileStorageService.getBaseUrl()
//                                                + "images/thumb_abc.png"); // review#1 first image's thumbnail
//
//    sendRemoveRequest(1L, 1L);
//    reviewBookmarks = bookmarkController.getReviewBookmarkList(1L);
//    assertThat(reviewBookmarks).isEmpty();
//  }
//
//
//  @Test
//  void removeBookmarkFail() {
//    sendCreateRequest(ItemType.REVIEW, 1L, 1L);
//    List<BookmarkBriefResponse> reviewBookmarks = bookmarkController.getReviewBookmarkList(1L);
//    assertThat(reviewBookmarks).hasSize(1);
//
//    // invalid bookmark id
//    assertThatThrownBy(() -> sendRemoveRequest(1L, 19L)).isInstanceOf(NotFoundException.class);
//    reviewBookmarks = bookmarkController.getReviewBookmarkList(1L);
//    assertThat(reviewBookmarks).hasSize(1);
//
//    // other member's bookmark
//    assertThatThrownBy(() -> sendRemoveRequest(2L, 1L)).isInstanceOf(ForbiddenException.class);
//    reviewBookmarks = bookmarkController.getReviewBookmarkList(1L);
//    assertThat(reviewBookmarks).hasSize(1);
//  }
//
//  @Test
//  void getReviewBookmarkDetailTest() {
//    sendCreateRequest(ItemType.REVIEW, 1L, 1L);
//    ReviewBookmarkResponse reviewBookmark = bookmarkController.getReviewBookmark(1L, 1L);
//    assertThat(reviewBookmark).extracting("id").isEqualTo(1L);
//    assertThat(reviewBookmark).extracting("review")
//                              .extracting("id")
//                              .isEqualTo(1L);
//  }
//
//  @Test
//  void getPlaceBookmarkDetailTest() {
//    sendCreateRequest(ItemType.PLACE, 1L, 1L);
//    PlaceBookmarkResponse placeBookmark = bookmarkController.getPlaceBookmark(1L, 1L);
//    assertThat(placeBookmark).extracting("id").isEqualTo(1L);
//    assertThat(placeBookmark).extracting("place")
//                             .extracting("name")
//                             .isEqualTo("히든아워");
//  }
//
//  @Test
//  void getBookmarkDetailFail() {
//    sendCreateRequest(ItemType.REVIEW, 1L, 1L);
//    sendCreateRequest(ItemType.PLACE, 1L, 1L);
//    assertThat(bookmarkController.getReviewBookmarkList(1L)).extracting("id").containsExactly(1L);
//    assertThat(bookmarkController.getPlaceBookmarkList(1L)).extracting("id").containsExactly(2L);
//
//    // invalid bookmark id
//    assertThatThrownBy(() -> bookmarkController.getReviewBookmark(1L, 10L)).isInstanceOf(
//        NotFoundException.class);
//    assertThatThrownBy(() -> bookmarkController.getPlaceBookmark(1L, 10L)).isInstanceOf(
//        NotFoundException.class);
//
//    // other member's bookmark
//    assertThatThrownBy(() -> bookmarkController.getReviewBookmark(2L, 1L)).isInstanceOf(
//        ForbiddenException.class);
//    assertThatThrownBy(() -> bookmarkController.getPlaceBookmark(2L, 2L)).isInstanceOf(
//        ForbiddenException.class);
//  }
//
//
//  private void sendCreateRequest(ItemType type, Long memberId, Long itemId) {
//    BookmarkCreateRequest request = new BookmarkCreateRequest();
//    request.setItemId(itemId);
//    bookmarkController.createBookmark(memberId, type, request);
//  }
//
//  private BookmarkRemovedResponse sendRemoveRequest(Long memberId, Long bookmarkId) {
//    return bookmarkController.removeBookmark(memberId, bookmarkId);
//  }
}
