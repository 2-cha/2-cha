package com._2cha.demo.auth.dto;

import com._2cha.demo.member.domain.Role;
import lombok.Data;


@Data
public class JwtAccessTokenPayload extends JwtTokenPayload {

  private String name;
  private String email;
  private Role role;

  public JwtAccessTokenPayload(Long sub, String email, String name, Role role) {
    super();
    this.setSub(sub);
    this.name = name;
    this.email = email;
    this.role = role;
  }
}
