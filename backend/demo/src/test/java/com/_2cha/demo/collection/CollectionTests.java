package com._2cha.demo.collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com._2cha.demo.collection.controller.CollectionController;
import com._2cha.demo.collection.domain.Collection;
import com._2cha.demo.collection.dto.CollectionCreateRequest;
import com._2cha.demo.collection.dto.CollectionCreatedResponse;
import com._2cha.demo.collection.dto.CollectionRemovedResponse;
import com._2cha.demo.collection.dto.CollectionUpdateRequest;
import com._2cha.demo.collection.dto.CollectionUpdateReviewRequest;
import com._2cha.demo.collection.dto.CollectionUpdatedResponse;
import com._2cha.demo.collection.dto.CollectionViewResponse;
import com._2cha.demo.collection.repository.CollectionQueryRepository;
import com._2cha.demo.global.exception.BadRequestException;
import com._2cha.demo.global.exception.ForbiddenException;
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
  }

  @Test
  void createTest() {
    List<Long> reviewIds = new ArrayList<>();
    reviewIds.add(1L);
    sendCreateRequest(1L, reviewIds, "Test Collection", " ", "https://dummy.img");

    List<CollectionViewResponse> memberCollections = collectionController.getMemberCollections(1L);
    assertThat(memberCollections).extracting("title").containsExactly("Test Collection");
  }

  @Test
  void createFail() {

    //fail with other member's review
    List<Long> reviewIds = new ArrayList<>();
    reviewIds.add(1L);
    assertThatThrownBy(() -> sendCreateRequest(2L, reviewIds, "title", " ", "https://dummy.img"))
        .isInstanceOf(ForbiddenException.class);

    //fail with empty title
    assertThatThrownBy(() -> sendCreateRequest(1L, reviewIds, "   ", " ", "https://dummy.img"))
        .isInstanceOf(BadRequestException.class);

    //fail with empty thumbnail
    assertThatThrownBy(() -> sendCreateRequest(1L, reviewIds, "title", " ", " "))
        .isInstanceOf(BadRequestException.class);

    //fail with empty reviews
    reviewIds.clear();
    assertThatThrownBy(
        () -> sendCreateRequest(1L, reviewIds, "title", "description", "http://dummy.img"))
        .isInstanceOf(BadRequestException.class);
  }

  @Test
  void privateCollectionTest() {
    List<Long> reviewIds = new ArrayList<>();
    reviewIds.add(1L);
    sendCreateRequest(1L, reviewIds, "Test Collection", " ", "https://dummy.img");
    sendUpdateRequest(1L, 1L, null, null, null, false);

    List<CollectionViewResponse> memberCollections;
    memberCollections = collectionController.getCollectionsIncludingPrivate(1L);
    assertThat(memberCollections).extracting("title").containsExactly("Test Collection");

    memberCollections = collectionController.getMemberCollections(1L);
    assertThat(memberCollections).isEmpty();
  }

  @Test
  void updateTitleTest() {
    List<Long> reviewIds = new ArrayList<>();
    reviewIds.add(1L);
    sendCreateRequest(1L, reviewIds, "Test Collection", " ", "https://dummy.img");

    List<CollectionViewResponse> memberCollections = collectionController.getMemberCollections(1L);
    assertThat(memberCollections).extracting("title").containsExactly("Test Collection");

    sendUpdateRequest(1L, 1L, "New Collection", null, null, null);
    memberCollections = collectionController.getMemberCollections(1L);
    assertThat(memberCollections).hasSize(1);
    assertThat(memberCollections).extracting("title").containsExactly("New Collection");
    assertThat(memberCollections).extracting("description").containsExactly(" ");
    assertThat(memberCollections).extracting("thumbnail").containsExactly("https://dummy.img");

    //TODO: fail
  }

  @Test
  void updateDescriptionTest() {
    List<Long> reviewIds = new ArrayList<>();
    reviewIds.add(1L);
    sendCreateRequest(1L, reviewIds, "Test Collection", " ", "https://dummy.img");

    List<CollectionViewResponse> memberCollections = collectionController.getMemberCollections(1L);
    assertThat(memberCollections).extracting("title").containsExactly("Test Collection");

    sendUpdateRequest(1L, 1L, null, "Short Description", null, null);
    memberCollections = collectionController.getMemberCollections(1L);
    assertThat(memberCollections).hasSize(1);
    assertThat(memberCollections).extracting("title").containsExactly("Test Collection");
    assertThat(memberCollections).extracting("description").containsExactly("Short Description");
    assertThat(memberCollections).extracting("thumbnail").containsExactly("https://dummy.img");

    //TODO: fail
  }

  @Test
  void updateThumbnailTest() {
    List<Long> reviewIds = new ArrayList<>();
    reviewIds.add(1L);
    sendCreateRequest(1L, reviewIds, "Test Collection", " ", "https://dummy.img");

    List<CollectionViewResponse> memberCollections = collectionController.getMemberCollections(1L);
    assertThat(memberCollections).extracting("title").containsExactly("Test Collection");

    sendUpdateRequest(1L, 1L, null, null, "https://new.img", null);
    memberCollections = collectionController.getMemberCollections(1L);
    assertThat(memberCollections).hasSize(1);
    assertThat(memberCollections).extracting("title").containsExactly("Test Collection");
    assertThat(memberCollections).extracting("description").containsExactly(" ");
    assertThat(memberCollections).extracting("thumbnail").containsExactly("https://new.img");

    //TODO: fail
  }

  @Test
  void updateExposureTest() {
    List<Long> reviewIds = new ArrayList<>();
    reviewIds.add(1L);
    sendCreateRequest(1L, reviewIds, "Test Collection", " ", "https://dummy.img");

    List<CollectionViewResponse> memberCollections = collectionController.getMemberCollections(1L);
    assertThat(memberCollections).extracting("title").containsExactly("Test Collection");

    sendUpdateRequest(1L, 1L, null, null, null, false);
    memberCollections = collectionController.getMemberCollections(1L);
    assertThat(memberCollections).isEmpty();

    memberCollections = collectionController.getCollectionsIncludingPrivate(1L);
    assertThat(memberCollections).hasSize(1);
    assertThat(memberCollections).extracting("title").containsExactly("Test Collection");
    assertThat(memberCollections).extracting("description").containsExactly(" ");
    assertThat(memberCollections).extracting("thumbnail").containsExactly("https://dummy.img");

    //TODO: fail
  }

  @Test
  void updateFail() {
    List<Long> reviewIds = new ArrayList<>();
    reviewIds.add(1L);
    sendCreateRequest(1L, reviewIds, "Test Collection", " ", "https://dummy.img");

    List<CollectionViewResponse> memberCollections = collectionController.getMemberCollections(1L);
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
    sendCreateRequest(1L, reviewIds, "Test Collection", " ", "https://dummy.img");

    List<CollectionViewResponse> memberCollections;
    memberCollections = collectionController.getMemberCollections(1L);
    assertThat(memberCollections).hasSize(1);

    sendRemoveRequest(1L, 1L);
    memberCollections = collectionController.getMemberCollections(1L);
    assertThat(memberCollections).isEmpty();
  }

  @Test
  void removeFail() {
    List<Long> reviewIds = new ArrayList<>();
    reviewIds.add(1L);
    sendCreateRequest(1L, reviewIds, "Test Collection", " ", "https://dummy.img");

    List<CollectionViewResponse> memberCollections;
    memberCollections = collectionController.getMemberCollections(1L);
    assertThat(memberCollections).hasSize(1);

    // remove other member's collection
    assertThatThrownBy(() -> sendRemoveRequest(2L, 1L)).isInstanceOf(ForbiddenException.class);

    memberCollections = collectionController.getMemberCollections(1L);
    assertThat(memberCollections).hasSize(1);
  }

  //TODO
  @Test
  void updateReviewsTest() {
    final SessionImplementor session = em.unwrap(SessionImplementor.class);
    final PersistenceContext pc = session.getPersistenceContext();

    em.flush();
    List<Long> reviewIds = new ArrayList<>();
    reviewIds.add(1L);
    reviewIds.add(3L);
    sendCreateRequest(1L, reviewIds, "Member1 Collection", " ", "https://dummy.img");

//    CollectionReviewsResponse collection1;
//    collection1 = collectionController.getCollectionDetail(1L, 1L);

    //!=========================================================================================!//

    em.flush();
    em.clear();
    Collection coll = em.createQuery(
                            "select c from Collection c "
                            + "join fetch c.reviews cr "
                            + "join fetch cr.review r where c.id = :collId ",
                            Collection.class)
                        .setParameter("collId", 1L)
                        .getSingleResult();

    reviewIds.clear();
    reviewIds = coll.getReviews().stream().map(cr -> cr.getReview().getId()).toList();

    System.out.println("coll = " + coll);
    //!=========================================================================================!//

    assertThat(coll.getReviews()).extracting("id")
                                 .containsExactly(1L, 3L);
  }

  //TODO
  @Test
  void updateReviewsFail() {
    List<Long> reviewIds = new ArrayList<>();
    reviewIds.add(1L);
    reviewIds.add(3L);
    sendCreateRequest(1L, reviewIds, "Member1 Collection", " ", "https://dummy.img");

    List<CollectionViewResponse> memberCollections;
    memberCollections = collectionController.getMemberCollections(1L);
    assertThat(memberCollections).extracting("id")
                                 .containsExactly(1L, 3L);

    reviewIds.add(2L);
    reviewIds.remove(1L);
    sendUpdateReviewsRequest(1L, 1L, reviewIds);
    memberCollections = collectionController.getMemberCollections(1L);
    assertThat(memberCollections).extracting("id")
                                 .containsExactly(2L, 3L);
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


  public CollectionRemovedResponse sendRemoveRequest(Long memberId, Long collId) {
    return collectionController.removeCollection(memberId, 1L);
  }


  public CollectionUpdatedResponse sendUpdateReviewsRequest(Long memberId, Long collId,
                                                            List<Long> reviewIds) {
    CollectionUpdateReviewRequest request = new CollectionUpdateReviewRequest();
    request.setReviewIds(reviewIds);

    return collectionController.updateReviews(memberId, collId, request);
  }
}
