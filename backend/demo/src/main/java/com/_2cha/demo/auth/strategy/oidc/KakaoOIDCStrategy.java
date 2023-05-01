package com._2cha.demo.auth.strategy.oidc;

import com._2cha.demo.auth.dto.OIDCUserProfile;
import com._2cha.demo.member.domain.OIDCProvider.Values;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Component;

//TODO
@Component(Values.KAKAO)
public class KakaoOIDCStrategy implements OIDCStrategy {

  @Override
  public String getIdToken(String authCode) {
    return null;
  }

  @Override
  public OIDCUserProfile getProfile(String idToken) throws JsonProcessingException {
    return null;
  }

  @Override
  public boolean validateIdToken(String idToken) {
    return OIDCStrategy.super.validateIdToken(idToken);
  }

  @Override
  public OIDCUserProfile authenticate(String code) {
    return OIDCStrategy.super.authenticate(code);
  }
}
