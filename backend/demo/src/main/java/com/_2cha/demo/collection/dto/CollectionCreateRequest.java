package com._2cha.demo.collection.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class CollectionCreateRequest {

  @NotEmpty
  @NotBlank
  String title;

  @NotNull
  String description;

  @NotEmpty
  @NotBlank
  String thumbnail;

  @NotEmpty
  List<Long> reviewIds;
}
