package com._2cha.demo.global.converter;

import com._2cha.demo.member.domain.OIDCProvider;
import lombok.SneakyThrows;
import org.springframework.core.convert.converter.Converter;

public class PathToProviderEnumConverter implements Converter<String, OIDCProvider> {

  @SneakyThrows
  @Override
  public OIDCProvider convert(String source) {
    try {
      return OIDCProvider.valueOf(source.toUpperCase());
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
}
