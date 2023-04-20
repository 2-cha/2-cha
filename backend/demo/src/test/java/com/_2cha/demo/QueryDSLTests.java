package com._2cha.demo;

import static com._2cha.demo.member.domain.QMember.member;

import com._2cha.demo.member.domain.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
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
public class QueryDSLTests {

  @Autowired
  EntityManager em;

  @BeforeEach
  public void before() {
    Member admin = Member.createMember("admin@2cha.com", "1234", "admin");
    em.persist(admin);
  }

  @Test
  void test() {
    JPAQueryFactory queryFactory = new JPAQueryFactory(em);

    Member mem = queryFactory.select(member)
                             .from(member)
                             .where(member.name.eq("admin"))
                             .fetchOne();
    Assertions.assertThat(mem.getEmail()).isEqualTo("admin@2cha.com");
  }
}
