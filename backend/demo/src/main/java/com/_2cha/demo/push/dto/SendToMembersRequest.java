package com._2cha.demo.push.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Data;

@Data
public class SendToMembersRequest {

  @NotEmpty
  List<Long> memberIds;
  private PayloadWithoutTarget payload;
}
