package com._2cha.demo.place.dto;

import com._2cha.demo.place.domain.Category;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
@JsonNaming(SnakeCaseStrategy.class)
public class NearbyPlaceWithCategoryFilterRequest extends NearbyPlaceRequest {


  @NotNull
  private List<Category> categories;
}