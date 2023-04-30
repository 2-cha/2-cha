package com._2cha.demo.bookmark.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookmarkCreateRequest {

  @NotNull
  private Long itemId;
}
