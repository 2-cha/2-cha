package com._2cha.demo.place.dto;

import com._2cha.demo.place.domain.Category;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
@JsonNaming(SnakeCaseStrategy.class)
public class NearbyPlaceSearchParams {

  private Double lat;
  private Double lon;
  private Double maxDist;
  private FilterBy filterBy;
  private List<Object> filterValues = new ArrayList<>();
  private SortBy sortBy;
  private Long offset;
  private Integer pageSize;


  public NearbyPlaceSearchParams(Double lat, Double lon, Double maxDist, FilterBy filterBy,
                                 List<String> rawFilterValues, SortBy sortBy, Long offset,
                                 Integer pageSize) {
    this.lat = lat;
    this.lon = lon;
    this.maxDist = maxDist;
    this.filterBy = filterBy;
    convertFilterValues(rawFilterValues);
    this.sortBy = sortBy;
    this.offset = offset;
    this.pageSize = pageSize;
  }

  private void convertFilterValues(List<String> rawFilterValues) {
    if (rawFilterValues == null || rawFilterValues.isEmpty()) return;
    if (filterBy == null) {
      throw new IllegalStateException("Cannot set filter values without setting filterBy");
    }
    if (filterValues != null) {
      for (String val : rawFilterValues) {
        Object converted = switch (filterBy) {
          case DEFAULT -> val;
          case TAG -> Long.valueOf(val);
          case CATEGORY -> Category.valueOf(val);
        };
        filterValues.add(converted);
      }
    }
  }
}

