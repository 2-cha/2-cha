package com._2cha.demo.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RelationshipOperationResponse {

  private Long followerId;
  private Long followingId;
}
