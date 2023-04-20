package com._2cha.demo.member.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ToggleAchievementExposureResponse {

  @NotNull
  private Long achvId;

  @NotNull
  boolean isExposed;
}
