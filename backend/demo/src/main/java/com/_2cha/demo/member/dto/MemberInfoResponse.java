package com._2cha.demo.member.dto;


import com._2cha.demo.member.domain.Member;
import com._2cha.demo.member.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberInfoResponse {

  private Long id;
  private String email;
  private String name;
  private Role role;

  public MemberInfoResponse(Member member) {
    this.id = member.getId();
    this.email = member.getEmail();
    this.name = member.getName();
    this.role = member.getRole();
  }
}
