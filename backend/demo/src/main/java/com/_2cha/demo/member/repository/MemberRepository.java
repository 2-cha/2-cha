package com._2cha.demo.member.repository;

import com._2cha.demo.member.domain.Member;
import com._2cha.demo.member.domain.OIDCProvider;

public interface MemberRepository {

  void save(Member member);

  Member findByName(String name);

  Member findById(Long id);

  Member findByOIDCId(OIDCProvider oidcProvider, String oidcId);

  Member findByEmail(String email);
}
