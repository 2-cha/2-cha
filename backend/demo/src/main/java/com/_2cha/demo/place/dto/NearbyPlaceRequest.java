package com._2cha.demo.place.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
@JsonNaming(SnakeCaseStrategy.class)
public class NearbyPlaceRequest {

  @NotNull
  private Double lat;

  @NotNull
  private Double lon;

  @NotNull
  private Double minDist;

  @NotNull
  private Double maxDist;

  @NotNull
  private FilterBy filterBy;

  private List<String> filterValues;

  @NotNull
  private SortBy sortBy;
}

