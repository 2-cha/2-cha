package com._2cha.demo.member.dto;

import com._2cha.demo.member.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberCredResponse {

  private Long id;
  private String email;
  private String name;
  private String password;
  private Role role;
}
