package com._2cha.demo.push.repository;


import com._2cha.demo.push.domain.PushSubject;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PushSubjectRepository extends JpaRepository<PushSubject, Long> {

  List<PushSubject> findAllByMemberId(Long memberId);

  PushSubject findByValue(String value);
}
