package com._2cha.demo.member.repository;

import com._2cha.demo.member.domain.Achievement;
import org.springframework.data.repository.Repository;

public interface AchievementRepository extends Repository<Achievement, Long> {

  void save(Achievement achv);

  Achievement findAchievementById(Long achvId);
}
