package com._2cha.demo.collection.domain;

import com._2cha.demo.review.dto.TagCountResponse;
import com._2cha.demo.review.service.ReviewService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeFromIndexedValueContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class TopTagFetcherBridge implements ValueBridge<IdListWrapper, String> {

  private ReviewService reviewService;

  @Lazy
  @Autowired
  public void setReviewService(ReviewService reviewService) {
    this.reviewService = reviewService;
  }

  @Override
  public String toIndexedValue(IdListWrapper val, ValueBridgeToIndexedValueContext context) {
    List<TagCountResponse> topTagCountsByIdIn = reviewService.getTopTagCountsByIdIn(val.ids, 3);
    if (log.isDebugEnabled()) {
      log.debug("====================================");
      topTagCountsByIdIn.forEach(
          tagCountResponse -> log.debug("tagCountResponse: {}", tagCountResponse));
      log.debug("====================================");
    }
    return String.join(" ", topTagCountsByIdIn.stream().map(TagCountResponse::getMessage).toList());
  }

  @Override
  public IdListWrapper fromIndexedValue(String value, ValueBridgeFromIndexedValueContext context) {
    return new IdListWrapper(List.of());
  }
}


