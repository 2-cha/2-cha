package com._2cha.demo.member.dto;

import lombok.Data;

@Data
public class MemberInfoWithCredResponse {

  private MemberInfoResponse memberInfoResponse;
  private String password;
}
