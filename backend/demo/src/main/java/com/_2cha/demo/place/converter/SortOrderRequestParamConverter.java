package com._2cha.demo.place.converter;

import com._2cha.demo.place.dto.SortOrder;
import org.springframework.core.convert.converter.Converter;

public class SortOrderRequestParamConverter implements Converter<String, SortOrder> {

  @Override
  public SortOrder convert(String source) {
    return SortOrder.valueOf(source.toUpperCase());
  }
}
