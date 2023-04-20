package com._2cha.demo.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberProfileResponse {

  private Long id;
  private String name;
  private String profImg;
  private String profMsg;
}
