package com._2cha.demo.auth.dto;

import lombok.Data;

@Data
public class TokenResponse {

  private String accessToken;
  private String refreshToken;
}
