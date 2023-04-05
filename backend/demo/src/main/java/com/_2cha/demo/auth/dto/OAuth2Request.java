package com._2cha.demo.auth.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class OAuth2Request {

  @NotEmpty
  private String code;
}
