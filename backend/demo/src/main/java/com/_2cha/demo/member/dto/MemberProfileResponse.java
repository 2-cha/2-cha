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

  public MemberProfileResponse(Long id, String name, String profThumbUrlPath, String profMsg,
                               String baseUrl) {
    this.id = id;
    this.name = name;
    this.profImg = profThumbUrlPath != null ? baseUrl + profThumbUrlPath : null;
    this.profMsg = profMsg;
  }
}
