package com._2cha.demo.push.service;

import java.util.List;
import lombok.Getter;

@Getter
public class FcmOpResult {

  private final Integer successCount;
  private final Integer failureCount;
  private final List<FailedOp> failures;

  public FcmOpResult(Integer successCount, Integer failureCount,
                     List<FailedOp> failures) {
    this.successCount = successCount;
    this.failureCount = failureCount;
    this.failures = failures;
  }
}
