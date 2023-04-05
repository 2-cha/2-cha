package com._2cha.demo.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SignInWithAccountRequest {

  @NotEmpty
  @Email
  String email;

  @NotEmpty
  String password;
}
