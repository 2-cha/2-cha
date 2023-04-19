package com._2cha.demo.global.converter;

import com._2cha.demo.member.domain.OIDCProvider;
import org.springframework.core.convert.converter.Converter;

public class PathToProviderEnumConverter implements Converter<String, OIDCProvider> {

  @Override
  public OIDCProvider convert(String source) {
    return OIDCProvider.valueOf(source.toUpperCase());
  }
}
