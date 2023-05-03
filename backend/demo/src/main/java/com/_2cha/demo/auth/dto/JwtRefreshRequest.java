package com._2cha.demo.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class JwtRefreshRequest {

  @NotEmpty
  @NotBlank
  private String refreshToken;
}
