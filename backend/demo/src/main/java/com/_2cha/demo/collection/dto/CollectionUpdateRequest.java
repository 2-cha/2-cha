package com._2cha.demo.collection.dto;

import com._2cha.demo.collection.validator.NullOrNotBlank;
import lombok.Data;

/**
 * for PATCH method.
 * <p>
 * each field can be null, meaning no update required
 */
@Data
public class CollectionUpdateRequest {

  @NullOrNotBlank
  private String title;

  // allow empty string
  private String description;

  @NullOrNotBlank
  private String thumbnail;

  private Boolean exposure;
}
