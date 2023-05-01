package com._2cha.demo.auth.dto;


import com._2cha.demo.member.domain.Role;
import lombok.Data;

@Data
public class JwtTokenPayload {

  private Long sub;
  private String name;
  private String email;
  private Role role;

  public JwtTokenPayload(Long sub, String email, String name, Role role) {
    this.sub = sub;
    this.name = name;
    this.email = email;
    this.role = role;
  }
}
