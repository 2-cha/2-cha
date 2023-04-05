package com._2cha.demo.member.dto;

import java.util.List;
import lombok.Data;

@Data
public class RelationshipResponse {

  List<MemberInfoResponse> followers;
  List<MemberInfoResponse> followings;
}
