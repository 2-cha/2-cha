package com._2cha.demo.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SignUpRequest {

  @NotEmpty
  @Email
  String email;

  @NotEmpty
  String name;

  @NotEmpty
  String password;
}
