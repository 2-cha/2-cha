package com._2cha.demo.member.dto;

import lombok.Data;

@Data
public class MemberProfileResponse {

  private Long id;
  private String name;
  private String profImg;
  private String profMsg;
}
