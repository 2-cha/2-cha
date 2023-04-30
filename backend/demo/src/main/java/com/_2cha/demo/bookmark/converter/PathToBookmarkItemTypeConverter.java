package com._2cha.demo.bookmark.converter;

import com._2cha.demo.bookmark.domain.ItemType;
import org.springframework.core.convert.converter.Converter;

public class PathToBookmarkItemTypeConverter implements Converter<String, ItemType> {

  @Override
  public ItemType convert(String source) {
    return ItemType.valueOf(source.toUpperCase());
  }
}
