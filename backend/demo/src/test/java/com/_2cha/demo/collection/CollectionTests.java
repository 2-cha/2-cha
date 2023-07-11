package com._2cha.demo.collection;

import static com._2cha.demo.review.domain.Category.ACTIVITY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com._2cha.demo.collection.controller.CollectionController;
import com._2cha.demo.collection.dto.CollectionBriefResponse;
import com._2cha.demo.collection.dto.CollectionCreateRequest;
import com._2cha.demo.collection.dto.CollectionCreatedResponse;
import com._2cha.demo.collection.dto.CollectionDetailResponse;
import com._2cha.demo.collection.dto.CollectionReviewsUpdateRequest;
import com._2cha.demo.collection.dto.CollectionReviewsUpdatedResponse;
import com._2cha.demo.collection.dto.CollectionUpdateRequest;
import com._2cha.demo.collection.dto.CollectionUpdatedResponse;
import com._2cha.demo.collection.repository.CollectionQueryRepository;
import com._2cha.demo.global.exception.BadRequestException;
import com._2cha.demo.global.exception.ForbiddenException;
import com._2cha.demo.global.infra.storage.service.FileStorageService;
import com._2cha.demo.member.domain.Member;
import com._2cha.demo.place.domain.Category;
import com._2cha.demo.place.domain.Place;
import com._2cha.demo.review.domain.Tag;
import com._2cha.demo.review.service.ReviewService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.hibernate.engine.spi.PersistenceContext;
import org.hibernate.engine.spi.SessionImplementor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles({"test"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
public class CollectionTests {

  @Autowired
  EntityManager em;
  @Autowired
  Validator validator;
  @Autowired
  CollectionController collectionController;
  @Autowired
  CollectionQueryRepository collectionQueryRepository;
  @Autowired
  ReviewService reviewService;
  @Autowired
  FileStorageService fileStorageService;

  JPAQueryFactory queryFactory;

  @BeforeEach
  void mockUp() {
    String baseUrl = fileStorageService.getBaseUrl();
    this.queryFactory = new JPAQueryFactory(em);

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
    reviewService.writeReview(1L, 3L, Arrays.stream(tagIds).toList(), Arrays.stream(imgs).toList());
    reviewService.writeReview(1L, 4L, Arrays.stream(tagIds).toList(), Arrays.stream(imgs).toList());
    // member2 review
    reviewService.writeReview(2L, 2L, Arrays.stream(tagIds).toList(), Arrays.stream(imgs).toList());
  }

  @Test
  void createTest() {
    List<Long> reviewIds = new ArrayList<>();
    reviewIds.add(1L);
    sendCreateRequest(1L, reviewIds, "Test Collection", " ",
                      fileStorageService.getBaseUrl() + "images/1.png");

    List<CollectionBriefResponse> memberCollections = collectionController.getMemberCollections(1L,
                                                                                                1L);
    assertThat(memberCollections).extracting("title").containsExactly("Test Collection");
  }

  @Test
  void createFail() {

    //fail with other member's review
    List<Long> reviewIds = new ArrayList<>();
    reviewIds.add(1L);
    assertThatThrownBy(() -> sendCreateRequest(2L, reviewIds, "title", " ",
                                               fileStorageService.getBaseUrl() + "images/1.png"))
        .isInstanceOf(ForbiddenException.class);

    //fail with empty title
    assertThatThrownBy(() -> sendCreateRequest(1L, reviewIds, "   ", " ",
                                               fileStorageService.getBaseUrl() + "images/1.png"))
        .isInstanceOf(BadRequestException.class);

    //fail with empty thumbnail
    assertThatThrownBy(() -> sendCreateRequest(1L, reviewIds, "title", " ", " "))
        .isInstanceOf(BadRequestException.class);

    //fail with empty reviews
    reviewIds.clear();
    assertThatThrownBy(
        () -> sendCreateRequest(1L, reviewIds, "title", "description",
                                fileStorageService.getBaseUrl() + "images/1.png"))
        .isInstanceOf(BadRequestException.class);

    //fail with invalid reviews
    reviewIds.clear();
    reviewIds.add(999L);
    assertThatThrownBy(
        () -> sendCreateRequest(1L, reviewIds, "title", "description",
                                fileStorageService.getBaseUrl() + "images/1.png"))
        .isInstanceOf(BadRequestException.class);
  }

  @Test
  void privateCollectionTest() {
    List<Long> reviewIds = new ArrayList<>();
    reviewIds.add(1L);
    sendCreateRequest(1L, reviewIds, "Test Collection", " ",
                      fileStorageService.getBaseUrl() + "images/1.png");
    sendUpdateRequest(1L, 1L, null, null, null, false);

    List<CollectionBriefResponse> memberCollections;
    memberCollections = collectionController.getCollectionsIncludingPrivate(1L);
    assertThat(memberCollections).extracting("title").containsExactly("Test Collection");

    memberCollections = collectionController.getMemberCollections(1L, 1L);
    assertThat(memberCollections).isEmpty();
  }

  @Test
  void updateTitleTest() {
    List<Long> reviewIds = new ArrayList<>();
    reviewIds.add(1L);
    sendCreateRequest(1L, reviewIds, "Test Collection", " ",
                      fileStorageService.getBaseUrl() + "images/thumb_1.png");

    List<CollectionBriefResponse> memberCollections = collectionController.getMemberCollections(1L,
                                                                                                1L);
    assertThat(memberCollections).extracting("title").containsExactly("Test Collection");

    sendUpdateRequest(1L, 1L, "New Collection", null, null, null);
    memberCollections = collectionController.getMemberCollections(1L, 1L);
    assertThat(memberCollections).hasSize(1);
    assertThat(memberCollections).extracting("title").containsExactly("New Collection");
    assertThat(memberCollections).extracting("description").containsExactly(" ");
    assertThat(memberCollections).extracting("thumbnail").containsExactly(
        fileStorageService.getBaseUrl() + "images/thumb_1.png");
  }

  @Test
  void updateDescriptionTest() {
    List<Long> reviewIds = new ArrayList<>();
    reviewIds.add(1L);
    sendCreateRequest(1L, reviewIds, "Test Collection", " ",
                      fileStorageService.getBaseUrl() + "images/thumb_1.png");

    List<CollectionBriefResponse> memberCollections = collectionController.getMemberCollections(1L,
                                                                                                1L);
    assertThat(memberCollections).extracting("title").containsExactly("Test Collection");

    sendUpdateRequest(1L, 1L, null, "Short Description", null, null);
    memberCollections = collectionController.getMemberCollections(1L, 1L);
    assertThat(memberCollections).hasSize(1);
    assertThat(memberCollections).extracting("title").containsExactly("Test Collection");
    assertThat(memberCollections).extracting("description").containsExactly("Short Description");
    assertThat(memberCollections).extracting("thumbnail").containsExactly(
        fileStorageService.getBaseUrl() + "images/thumb_1.png");
  }

  @Test
  void updateThumbnailTest() {
    List<Long> reviewIds = new ArrayList<>();
    reviewIds.add(1L);
    sendCreateRequest(1L, reviewIds, "Test Collection", " ",
                      fileStorageService.getBaseUrl() + "images/1.png");

    List<CollectionBriefResponse> memberCollections = collectionController.getMemberCollections(1L,
                                                                                                1L);
    assertThat(memberCollections).extracting("title").containsExactly("Test Collection");

    String newUrl = fileStorageService.getBaseUrl() + "images/new.png";
    sendUpdateRequest(1L, 1L, null, null, newUrl, null);
    memberCollections = collectionController.getMemberCollections(1L, 1L);
    assertThat(memberCollections).hasSize(1);
    assertThat(memberCollections).extracting("title").containsExactly("Test Collection");
    assertThat(memberCollections).extracting("description").containsExactly(" ");
    assertThat(memberCollections).extracting("thumbnail").containsExactly(newUrl);
  }

  @Test
  void updateExposureTest() {
    List<Long> reviewIds = new ArrayList<>();
    reviewIds.add(1L);
    sendCreateRequest(1L, reviewIds, "Test Collection", " ",
                      fileStorageService.getBaseUrl() + "images/thumb_1.png");

    List<CollectionBriefResponse> memberCollections = collectionController.getMemberCollections(1L,
                                                                                                1L);
    assertThat(memberCollections).extracting("title").containsExactly("Test Collection");

    sendUpdateRequest(1L, 1L, null, null, null, false);
    memberCollections = collectionController.getMemberCollections(1L, 1L);
    assertThat(memberCollections).isEmpty();

    memberCollections = collectionController.getCollectionsIncludingPrivate(1L);
    assertThat(memberCollections).hasSize(1);
    assertThat(memberCollections).extracting("title").containsExactly("Test Collection");
    assertThat(memberCollections).extracting("description").containsExactly(" ");
    assertThat(memberCollections).extracting("thumbnail").containsExactly(
        fileStorageService.getBaseUrl() + "images/thumb_1.png");
  }

  @Test
  void updateFail() {
    List<Long> reviewIds = new ArrayList<>();
    reviewIds.add(1L);
    sendCreateRequest(1L, reviewIds, "Test Collection", " ",
                      fileStorageService.getBaseUrl() + "images/1.png");

    List<CollectionBriefResponse> memberCollections = collectionController.getMemberCollections(1L,
                                                                                                1L);
    assertThat(memberCollections).extracting("title").containsExactly("Test Collection");

    // other member's collection
    assertThatThrownBy(() -> sendUpdateRequest(2L, 1L, null, null, null, null))
        .isInstanceOf(ForbiddenException.class);

    // with empty title
    assertThatThrownBy(() -> sendUpdateRequest(1L, 1L, " ", null, null, null))
        .isInstanceOf(BadRequestException.class);

    // with empty thumbnail
    assertThatThrownBy(() -> sendUpdateRequest(1L, 1L, null, null, " ", null))
        .isInstanceOf(BadRequestException.class);
  }

  @Test
  void removeTest() {
    List<Long> reviewIds = new ArrayList<>();
    reviewIds.add(1L);
    sendCreateRequest(1L, reviewIds, "Test Collection", " ",
                      fileStorageService.getBaseUrl() + "images/1.png");

    List<CollectionBriefResponse> memberCollections;
    memberCollections = collectionController.getMemberCollections(1L, 1L);
    assertThat(memberCollections).hasSize(1);

    sendRemoveRequest(1L, 1L);
    memberCollections = collectionController.getMemberCollections(1L, 1L);
    assertThat(memberCollections).isEmpty();
  }

  @Test
  void removeFail() {
    List<Long> reviewIds = new ArrayList<>();
    reviewIds.add(1L);
    sendCreateRequest(1L, reviewIds, "Test Collection", " ",
                      fileStorageService.getBaseUrl() + "images/1.png");

    List<CollectionBriefResponse> memberCollections;
    memberCollections = collectionController.getMemberCollections(1L, 1L);
    assertThat(memberCollections).hasSize(1);

    // remove other member's collection
    assertThatThrownBy(() -> sendRemoveRequest(2L, 1L)).isInstanceOf(ForbiddenException.class);

    memberCollections = collectionController.getMemberCollections(1L, 1L);
    assertThat(memberCollections).hasSize(1);
  }


  @Test
  void updateReviewsTest() {
    final SessionImplementor session = em.unwrap(SessionImplementor.class);
    final PersistenceContext pc = session.getPersistenceContext();

    List<Long> reviewIds = new ArrayList<>();
    reviewIds.add(1L);
    reviewIds.add(3L);
    sendCreateRequest(1L, reviewIds, "Member1 Collection", " ",
                      fileStorageService.getBaseUrl() + "images/1.png");

    CollectionDetailResponse collection1 = collectionController.getCollectionDetail(1L, 1L);
    assertThat(collection1.getReviews()).extracting("id")
                                        .containsExactly(1L, 3L);

    reviewIds.clear();
    reviewIds.add(2L);
    reviewIds.add(1L);
    sendUpdateReviewsRequest(1L, 1L, reviewIds);

    collection1 = collectionController.getCollectionDetail(1L, 1L);
    assertThat(collection1.getReviews()).extracting("id")
                                        .containsExactly(2L, 1L);
  }


  @Test
  void updateReviewsFail() {
    List<Long> reviewIds = new ArrayList<>();
    reviewIds.add(1L);
    reviewIds.add(3L);
    sendCreateRequest(1L, reviewIds, "Member1 Collection", " ",
                      fileStorageService.getBaseUrl() + "images/1.png");

    CollectionDetailResponse collection1 = collectionController.getCollectionDetail(1L, 1L);
    assertThat(collection1.getReviews()).extracting("id")
                                        .containsExactly(1L, 3L);

    // other member's review
    reviewIds.clear();
    reviewIds.add(4L);
    assertThatThrownBy(() -> sendUpdateReviewsRequest(1L, 1L, reviewIds))
        .isInstanceOf(ForbiddenException.class);

    // empty reviews
    reviewIds.clear();
    assertThatThrownBy(() -> sendUpdateReviewsRequest(1L, 1L, reviewIds))
        .isInstanceOf(BadRequestException.class);

    // invalid review id
    reviewIds.add(99L);
    assertThatThrownBy(() -> sendUpdateReviewsRequest(1L, 1L, reviewIds))
        .isInstanceOf(BadRequestException.class);
  }


  public CollectionCreatedResponse sendCreateRequest(Long memberId, List<Long> reviewIds,
                                                     String title,
                                                     String description,
                                                     String thumbnail) {
    CollectionCreateRequest request = new CollectionCreateRequest();
    request.setTitle(title);
    request.setDescription(description);
    request.setThumbnail(thumbnail);
    request.setReviewIds(reviewIds);
    Set<ConstraintViolation<CollectionCreateRequest>> violations = validator.validate(request);
    if (!violations.isEmpty()) throw new BadRequestException("");

    return collectionController.createCollection(memberId, request);
  }


  public CollectionUpdatedResponse sendUpdateRequest(Long memberId,
                                                     Long reviewId,
                                                     String title,
                                                     String description, String thumbnail,
                                                     Boolean exposure) {
    CollectionUpdateRequest request = new CollectionUpdateRequest();
    request.setTitle(title);
    request.setDescription(description);
    request.setThumbnail(thumbnail);
    request.setExposure(exposure);

    Set<ConstraintViolation<CollectionUpdateRequest>> violations = validator.validate(request);
    if (!violations.isEmpty()) throw new BadRequestException("");

    return collectionController.updateCollection(memberId, reviewId, request);
  }


  public void sendRemoveRequest(Long memberId, Long collId) {
    collectionController.removeCollection(memberId, collId);
  }


  public CollectionReviewsUpdatedResponse sendUpdateReviewsRequest(Long memberId, Long collId,
                                                                   List<Long> reviewIds) {
    CollectionReviewsUpdateRequest request = new CollectionReviewsUpdateRequest();
    request.setReviewIds(reviewIds);

    Set<ConstraintViolation<CollectionReviewsUpdateRequest>> violations = validator.validate(
        request);
    if (!violations.isEmpty()) throw new BadRequestException("");

    return collectionController.updateReviews(memberId, collId, request);
  }
}
