package com._2cha.demo.auth.dto;


import com._2cha.demo.member.domain.Role;
import lombok.Data;

@Data
public class JwtTokenPayload {

  Long sub;
  String name;
  String email;
  Role role;
}
