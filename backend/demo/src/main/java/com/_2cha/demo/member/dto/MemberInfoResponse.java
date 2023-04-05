package com._2cha.demo.member.dto;


import com._2cha.demo.member.domain.Role;
import lombok.Data;

@Data
public class MemberInfoResponse {

  Long id;
  String email;
  String name;
  Role role;
}
