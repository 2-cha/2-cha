package com._2cha.demo.push.repository;


import com._2cha.demo.push.domain.PushSubject;
import java.util.List;
import org.springframework.data.repository.Repository;

public interface PushSubjectRepository extends Repository<PushSubject, Long> {

  List<PushSubject> findAllByMemberId(Long memberId);

  PushSubject findByValue(String value);


  void deleteByValue(String value);

  void save(PushSubject pushSubject);

  void delete(PushSubject pushSubject);

  void deleteAll(List<PushSubject> pushSubject);
}
