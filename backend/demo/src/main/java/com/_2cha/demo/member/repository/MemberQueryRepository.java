package com._2cha.demo.member.repository;

import static com._2cha.demo.member.domain.QAchievement.achievement;
import static com._2cha.demo.member.domain.QMember.member;
import static com._2cha.demo.member.domain.QMemberAchievement.memberAchievement;
import static com._2cha.demo.member.domain.QRelationship.relationship;
import static com.querydsl.core.types.Projections.bean;
import static com.querydsl.core.types.Projections.constructor;

import com._2cha.demo.member.domain.OIDCProvider;
import com._2cha.demo.member.dto.AchievementResponse;
import com._2cha.demo.member.dto.MemberCredResponse;
import com._2cha.demo.member.dto.MemberInfoResponse;
import com._2cha.demo.member.dto.MemberProfileResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class MemberQueryRepository {


  @PersistenceContext
  private final EntityManager em;
  private final JPAQueryFactory queryFactory;

  public MemberQueryRepository(EntityManager em) {
    this.em = em;
    this.queryFactory = new JPAQueryFactory(this.em);
  }

  public MemberProfileResponse getMemberProfileById(Long id) {
    return queryFactory.select(constructor(MemberProfileResponse.class,
                                           member.id,
                                           member.name,
                                           member.profImg,
                                           member.profMsg
                                          ))
                       .from(member)
                       .where(member.id.eq(id))
                       .fetchOne();
  }

  public MemberInfoResponse getMemberInfoById(Long id) {
    return queryFactory.select(constructor(MemberInfoResponse.class,
                                           member.id,
                                           member.email,
                                           member.name,
                                           member.role
                                          ))
                       .from(member)
                       .where(member.id.eq(id))
                       .fetchOne();
  }

  public MemberInfoResponse getMemberInfoByOidcId(OIDCProvider provider, String id) {
    return queryFactory.select(constructor(MemberInfoResponse.class,
                                           member.id,
                                           member.email,
                                           member.name,
                                           member.role
                                          ))
                       .from(member)
                       .where(member.oidcProvider.eq(provider),
                              member.oidcId.eq(id))
                       .fetchOne();
  }

  public MemberCredResponse getMemberCredByEmail(String email) {
    return queryFactory.select(constructor(MemberCredResponse.class,
                                           member.id,
                                           member.email,
                                           member.name,
                                           member.password,
                                           member.role
                                          ))
                       .from(member)
                       .where(member.email.eq(email))
                       .fetchOne();
  }

  public List<MemberProfileResponse> getFollowersProfiles(Long memberId) {
    return queryFactory.selectDistinct(constructor(MemberProfileResponse.class,
                                                   member.id,
                                                   member.name,
                                                   member.profImg,
                                                   member.profMsg
                                                  ))
                       .from(relationship)
                       .join(relationship.follower, member)  //NOTE
                       .where(relationship.following.id.eq(memberId))
                       .fetch();
  }

  public List<MemberProfileResponse> getFollowingsProfiles(Long memberId) {
    return queryFactory.selectDistinct(constructor(MemberProfileResponse.class,
                                                   member.id,
                                                   member.name,
                                                   member.profImg,
                                                   member.profMsg
                                                  ))
                       .from(relationship)
                       .join(relationship.following, member) //NOTE
                       .where(relationship.follower.id.eq(memberId))
                       .fetch();
  }

  public List<AchievementResponse> getMemberAchievements(Long memberId, boolean exposedOnly) {

    return queryFactory.selectDistinct(bean(AchievementResponse.class,
                                            achievement.name,
                                            achievement.badgeUrl
                                           ))
                       .from(memberAchievement)
                       .join(memberAchievement.achievement, achievement)
                       .where(memberAchievement.member.id.eq(memberId),
                              isExposed(exposedOnly))
                       .fetch();
  }

  private BooleanExpression isExposed(boolean cond) {
    return cond ? memberAchievement.isExposed.eq(true) : null;
  }
}
