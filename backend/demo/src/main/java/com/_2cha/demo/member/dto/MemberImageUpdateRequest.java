package com._2cha.demo.member.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

@Data
public class MemberImageUpdateRequest {

  @URL
  @Length(max = 255)
  private String url;
}
