package com._2cha.demo.auth.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class OIDCRequest {

  @NotEmpty
  private String code;
}
