package com._2cha.demo.review;

import static com._2cha.demo.review.domain.Category.ACTIVITY;

import com._2cha.demo.review.controller.TagController;
import com._2cha.demo.review.domain.Tag;
import com._2cha.demo.review.dto.TagFuzzySearchResponse;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.assertj.core.api.Assertions;
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
class TagTests {

  @Autowired
  TagController tagController;

  @Autowired
  EntityManager em;

  @BeforeEach
  void mockUp() {
    em.persist(Tag.createTag("이야기 나누기 좋아요", "🗣️", ACTIVITY));
    em.persist(Tag.createTag("책 읽기 좋아요", "📖️", ACTIVITY));
  }


  @Test
  void fuzzySearchTagsByHangul() {
    List<TagFuzzySearchResponse> ㅈㅇㅇ = tagController.fuzzySearchTagsByHangul("ㅈㅇㅇ");
    Assertions.assertThat(ㅈㅇㅇ)
              .extracting("message")
              .containsExactly("이야기 나누기 좋아요", "책 읽기 좋아요");

    List<TagFuzzySearchResponse> ㅇㄱ = tagController.fuzzySearchTagsByHangul("ㅇㄱ");
    Assertions.assertThat(ㅇㄱ)
              .extracting("message")
              .containsExactly("이야기 나누기 좋아요", "책 읽기 좋아요");

    List<TagFuzzySearchResponse> ㅇㅇㄱ = tagController.fuzzySearchTagsByHangul("ㅇㅇㄱ");
    Assertions.assertThat(ㅇㅇㄱ)
              .extracting("message")
              .containsExactly("이야기 나누기 좋아요");

    List<TagFuzzySearchResponse> ㅇㅈ = tagController.fuzzySearchTagsByHangul("ㅇㅈ");
    Assertions.assertThat(ㅇㅈ)
              .extracting("message")
              .containsExactly("이야기 나누기 좋아요", "책 읽기 좋아요");

    List<TagFuzzySearchResponse> ㄴㅈ = tagController.fuzzySearchTagsByHangul("ㄴㅈ");
    Assertions.assertThat(ㄴㅈ)
              .extracting("message")
              .containsExactly("이야기 나누기 좋아요");
  }
}