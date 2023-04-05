package com._2cha.demo.auth.dto;

import com._2cha.demo.member.domain.OIDCProvider;
import lombok.Data;

@Data
public class OIDCUserProfile {

  private OIDCProvider provider;
  private String id;
  private String name;
  private String email;
}
