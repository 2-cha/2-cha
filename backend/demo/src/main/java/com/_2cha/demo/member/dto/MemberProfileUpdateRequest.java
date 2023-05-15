package com._2cha.demo.member.dto;

import com._2cha.demo.collection.validator.NullOrNotBlank;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class MemberProfileUpdateRequest {

  @NotBlank
  private String name;

  @NullOrNotBlank
  @Length(max = 255)
  private String profMsg;
}
