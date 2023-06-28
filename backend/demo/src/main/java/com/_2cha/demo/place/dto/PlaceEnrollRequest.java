package com._2cha.demo.place.dto;

import com._2cha.demo.place.domain.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class PlaceEnrollRequest {

  @NotBlank
  private String name;

  @NotNull
  private Category category;

  @NotNull
  @Range(min = -90, max = 90)
  private Double lat;

  @NotNull
  @Range(min = -180, max = 180)
  private Double lon;

  private String address;

  private String lotAddress;

  private String site;
}
