package com._2cha.demo.auth.strategy.oidc;

import com._2cha.demo.auth.config.KakaoOIDCConfig;
import com._2cha.demo.auth.dto.OIDCUserProfile;
import com._2cha.demo.member.domain.OIDCProvider;
import com._2cha.demo.member.domain.OIDCProvider.Values;
import com.auth0.jwt.JWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.Base64;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.BodyInserters.FormInserter;
import org.springframework.web.reactive.function.client.WebClient;

@Component(Values.KAKAO)
@RequiredArgsConstructor
public class KakaoOIDCStrategy implements OIDCStrategy {

  private final KakaoOIDCConfig config;
  private final ObjectMapper objectMapper;

  @Override
  public String getIdToken(String authCode) {
    WebClient webClient = WebClient.builder()
                                   .baseUrl(config.getTokenEndpoint())
                                   .defaultHeader(HttpHeaders.CONTENT_TYPE,
                                                  MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                   .build();

    FormInserter<String> inserter = BodyInserters.fromFormData("code", authCode)
                                                 .with("client_id", config.getClientId())
                                                 .with("client_secret", config.getClientSecret())
                                                 .with("redirect_uri", config.getRedirectUri())
                                                 .with("grant_type", config.getGrantType());

    Map<String, String> response = webClient.post()
                                            .body(inserter)
                                            .retrieve()
                                            .bodyToMono(
                                                new ParameterizedTypeReference<Map<String, String>>() {})
                                            .block();
    return response.get("id_token");
  }

  @Override
  public OIDCUserProfile getProfile(String idToken) throws JsonProcessingException {
    String base64payload = JWT.decode(idToken)
                              .getPayload();  // not decoded
    String payload = new String(Base64.getDecoder()
                                      .decode(base64payload));

    KakaoIdTokenPayload idTokenPayload = objectMapper.readValue(payload,
                                                                KakaoIdTokenPayload.class);

    OIDCUserProfile profile = new OIDCUserProfile();
    profile.setProvider(OIDCProvider.KAKAO);
    profile.setId(idTokenPayload.getSub());
    profile.setName(idTokenPayload.getNickname());
    profile.setEmail(idTokenPayload.getEmail());
    profile.setImageUrl(idTokenPayload.getPicture());
    return profile;
  }

  @Override
  public boolean validateIdToken(String idToken) {
    return OIDCStrategy.super.validateIdToken(idToken);
  }
}

@Getter
@Setter
@ToString
@JsonNaming(SnakeCaseStrategy.class)
class KakaoIdTokenPayload {

  private String aud;
  private String sub;
  private Long iat;
  private Long exp;
  private Long authTime;

  // profile scope
  private String nickname;
  private String picture;

  // email scope
  private String email;
  private boolean emailVerified;
}
