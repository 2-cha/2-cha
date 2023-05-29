package com._2cha.demo.push.repository;


import com._2cha.demo.push.domain.PushTopicSubscription;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PushTopicSubscriptionRepository extends
                                                 JpaRepository<PushTopicSubscription, Long> {

  List<PushTopicSubscription> findAllByMemberId(Long memberId);

  PushTopicSubscription findByMemberIdAndTopic(Long memberId, String topic);

  void deleteAllByMemberIdAndTopic(Long memberId, String topic);
}
