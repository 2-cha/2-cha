package com._2cha.demo.recommendation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCollectionPreferenceRepository extends
                                                      JpaRepository<MemberCollectionPreference, Long> {
}
