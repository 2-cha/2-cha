package com._2cha.demo.member.repository;

import com._2cha.demo.member.domain.Member;
import java.util.List;
import org.springframework.data.repository.Repository;

public interface MemberRepository extends Repository<Member, Long> {

  void save(Member member);

  Member findByName(String name);

  Member findById(Long id);

  Member findByEmail(String email);

  List<Member> findAllByNameIn(List<String> names);
}
