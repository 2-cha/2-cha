package com._2cha.demo.recommendation.repository;

import com._2cha.demo.recommendation.domain.MemberCollectionPreference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCollectionPreferenceRepository extends
                                                      JpaRepository<MemberCollectionPreference, Long> {

  MemberCollectionPreference findByMemberIdAndCollectionIdAndPreference(Long memberId, Long collId,
                                                                        Float preference);

  MemberCollectionPreference findByMemberIdAndCollectionId(Long memberId, Long collId);
}
