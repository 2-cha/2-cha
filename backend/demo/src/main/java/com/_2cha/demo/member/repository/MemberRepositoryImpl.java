package com._2cha.demo.member.repository;

import com._2cha.demo.member.domain.Member;
import com._2cha.demo.member.domain.OIDCProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements
                                  MemberRepository {

  private final EntityManager em;

  @Override
  public void save(Member member) {
    em.persist(member);
  }


  @Override
  public Member findById(Long id) {
    return em.find(Member.class, id);
  }

  @Override
  public Member findByOIDCId(OIDCProvider oidcProvider, String oidcId) {
    try {
      return em.createQuery("select m " +
                            "from Member m " +
                            "where oidcProvider = :oidcProvider " +
                            "and oidcId = :oidcId", Member.class)
               .setParameter("oidcProvider", oidcProvider)
               .setParameter("oidcId", oidcId)
               .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  @Override
  public Member findByName(String name) {
    try {
      return em.createQuery("select m " +
                            "from Member m " +
                            "where name like :name", Member.class)
               .setParameter("name", name)
               .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  @Override
  public Member findByEmail(String email) {
    try {
      return em.createQuery("select m " +
                            "from Member m " +
                            "where email like :email", Member.class)
               .setParameter("email", email)
               .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }
}
