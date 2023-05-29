package com._2cha.demo.review.repository;

import com._2cha.demo.review.domain.TagCreationRequest;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagCreationRequestRepository extends JpaRepository<TagCreationRequest, Long> {

  TagCreationRequest save(TagCreationRequest tagCreationRequest);

  List<TagCreationRequest> findAllByOrderByRequestedAtDesc(Pageable pageParam);
}
