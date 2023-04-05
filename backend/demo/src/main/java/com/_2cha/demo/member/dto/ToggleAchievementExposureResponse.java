package com._2cha.demo.member.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ToggleAchievementExposureResponse {

  @NotNull
  private Long achvId;

  @NotNull
  boolean isExposed;
}
