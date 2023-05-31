package com._2cha.demo.push.dto;

import com._2cha.demo.push.service.FailedOp;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class PushResponse {

  Integer totalCount;
  Integer successCount;
  Integer failureCount;
  List<FailedOp> failures;
}
