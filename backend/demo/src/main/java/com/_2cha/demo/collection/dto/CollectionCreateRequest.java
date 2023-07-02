package com._2cha.demo.collection.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CollectionCreateRequest {

  @NotEmpty
  @NotBlank
  @Length(max = 15)
  String title;

  @NotNull
  @Length(max = 15)
  String description;

  @NotEmpty
  @NotBlank
  String thumbnail;

  @NotEmpty
  List<Long> reviewIds;
}
