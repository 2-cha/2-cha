package com._2cha.demo.auth.strategy.oidc;


import com._2cha.demo.auth.config.GoogleOIDCConfig;
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
import org.springframework.web.util.UriComponentsBuilder;


@Component(Values.GOOGLE)
@RequiredArgsConstructor
public class GoogleOIDCStrategy implements OIDCStrategy {

  //NOTE: use singleton bean cause of expensive initializing
  private final ObjectMapper objectMapper;
  private final GoogleOIDCConfig config;


  public String getAuthCodeURI() {
    return UriComponentsBuilder.fromUriString(config.getOauthEndpoint())
                               .queryParam("client_id", config.getClientId())
                               .queryParam("redirect_uri", config.getRedirectUri())
                               .queryParam("response_type", config.getResponseType())
                               .queryParam("scope", config.getScope())
                               .build()
                               .toUriString();
  }

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

  //TODO
  @Override
  public boolean validateIdToken(String token) {
    return true;
  }

  @Override
  public OIDCUserProfile getProfile(String idToken) throws JsonProcessingException {
    String base64payload = JWT.decode(idToken)
                              .getPayload();  // not decoded
    String payload = new String(Base64.getUrlDecoder()
                                      .decode(base64payload));

    GoogleIdTokenPayload idTokenPayload = objectMapper.readValue(payload,
                                                                 GoogleIdTokenPayload.class);

    OIDCUserProfile profile = new OIDCUserProfile();
    profile.setProvider(OIDCProvider.GOOGLE);
    profile.setId(idTokenPayload.getSub());
    profile.setName(idTokenPayload.getName());
    profile.setEmail(idTokenPayload.getEmail());
    profile.setImageUrl(idTokenPayload.getPicture());
    return profile;
  }
}

@Getter
@Setter
@ToString
@JsonNaming(SnakeCaseStrategy.class)
class GoogleIdTokenPayload {

  private String azp;
  private String atHash;

  private String iss;
  private String aud;
  private String sub;
  private Long iat;
  private Long exp;

  // profile scope
  private String name;
  private String picture;
  private String givenName;
  private String familyName;
  private String locale;

  // email scope
  private String email;
  private boolean emailVerified;
}
