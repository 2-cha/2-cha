package com._2cha.demo.place.converter;

import com._2cha.demo.place.dto.SortBy;
import org.springframework.core.convert.converter.Converter;

public class SortByRequestParamConverter implements Converter<String, SortBy> {

  @Override
  public SortBy convert(String source) {
    return SortBy.valueOf(source.toUpperCase());
  }
}
