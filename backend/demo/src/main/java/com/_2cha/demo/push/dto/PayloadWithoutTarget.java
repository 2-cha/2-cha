package com._2cha.demo.push.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@JsonInclude(Include.NON_EMPTY)
public class PayloadWithoutTarget {

  @NotEmpty
  private String title;
  @NotEmpty
  private String body;
  private Object data;
}
