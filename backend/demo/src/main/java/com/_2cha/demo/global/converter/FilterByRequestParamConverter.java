package com._2cha.demo.global.converter;

import com._2cha.demo.place.dto.FilterBy;
import org.springframework.core.convert.converter.Converter;

public class FilterByRequestParamConverter implements Converter<String, FilterBy> {

  @Override
  public FilterBy convert(String source) {
    return FilterBy.valueOf(source.toUpperCase());
  }
}
