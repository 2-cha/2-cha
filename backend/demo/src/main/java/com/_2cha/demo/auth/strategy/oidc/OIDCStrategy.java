package com._2cha.demo.auth.strategy.oidc;

import com._2cha.demo.auth.dto.OIDCUserProfile;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.constraints.NotEmpty;

//TODO: nonce, state-token(CSRF), validate
public interface OIDCStrategy extends AuthStrategy {

  String getIdToken(String authCode);

  OIDCUserProfile getProfile(String idToken) throws JsonProcessingException;

  default boolean validateIdToken(String idToken) {
    return true;
  }

  default OIDCUserProfile authenticate(@NotEmpty String code) {
    OIDCUserProfile profile;

    String idToken = getIdToken(code);
    if (!validateIdToken(idToken)) {
      throw new RuntimeException("validateFail");
    }
    try {
      profile = getProfile(idToken);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    return profile;
  }
}
