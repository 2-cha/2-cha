package com._2cha.demo.auth.service;

import com._2cha.demo.auth.config.GoogleOIDCConfig;
import com._2cha.demo.auth.config.JwtConfig;
import com._2cha.demo.auth.dto.JwtTokenPayload;
import com._2cha.demo.auth.dto.OAuth2Request;
import com._2cha.demo.auth.dto.OIDCUserProfile;
import com._2cha.demo.auth.dto.SignInWithAccountRequest;
import com._2cha.demo.auth.dto.TokenResponse;
import com._2cha.demo.auth.strategy.oidc.GoogleOIDCStrategy;
import com._2cha.demo.auth.strategy.oidc.OIDCStrategy;
import com._2cha.demo.global.exception.NoSuchMemberException;
import com._2cha.demo.global.exception.UnauthorizedException;
import com._2cha.demo.member.domain.OIDCProvider;
import com._2cha.demo.member.dto.MemberInfoResponse;
import com._2cha.demo.member.dto.MemberInfoWithCredResponse;
import com._2cha.demo.member.service.MemberService;
import com._2cha.demo.util.BCryptHashingUtils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Field;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

  private final JwtConfig jwtConfig;
  private final GoogleOIDCConfig googleOIDCConfig;


  private final ObjectMapper objectMapper;  //thread-safe
  private final MemberService memberService; //TODO: use Member Service API


  /**
   * @param token
   * @return Decoded Payload
   * @throws JWTVerificationException
   * @throws JsonProcessingException
   */

  public JwtTokenPayload verifyJwt(String token) {

    Verification verification = JWT.require(Algorithm.HMAC512(jwtConfig.getKey()));

    Field[] claims = JwtTokenPayload.class.getDeclaredFields();

    Arrays.stream(claims).
          forEach(claim -> verification.withClaimPresence(claim.getName()));

    JWTVerifier verifier = verification
        .withIssuer(jwtConfig.getIssuer())
        .build();
    DecodedJWT jwt = verifier.verify(token);  // not decoded..!

    String payload = jwt.getPayload();
    String decodedPayload = new String(Base64.getDecoder().decode(payload));

    try {
      return objectMapper.readValue(decodedPayload, JwtTokenPayload.class);
    } catch (JsonProcessingException e) {
      throw new UnauthorizedException("Invalid payload format", "invalidJwtPayload");
    }
  }

  public String issueJwt(JwtTokenPayload payload, Integer lifetime) {
    return JWT.create()
              .withPayload(objectMapper.convertValue(payload, Map.class))
              .withIssuer(jwtConfig.getIssuer())
              .withSubject(payload.getSub().toString())
              .withExpiresAt(Instant.now().plus(lifetime, ChronoUnit.MINUTES))
              .sign(Algorithm.HMAC512(jwtConfig.getKey()));
  }

  //TODO
  public String refreshJwt(String token) {return "TODO";}

  public TokenResponse issueAccessTokenAndRefreshToken(JwtTokenPayload payload) {
    TokenResponse tokens = new TokenResponse();
    tokens.setAccessToken(issueJwt(payload, jwtConfig.getAccessLifetime()));
    tokens.setRefreshToken(issueJwt(payload, jwtConfig.getRefreshLifetime()));

    return tokens;
  }


  public TokenResponse signInWithAccount(SignInWithAccountRequest dto) {
    // email / password
    MemberInfoWithCredResponse response = memberService.getMemberInfoWithCred(
        dto.getEmail());
    if (response == null) {
      throw new UnauthorizedException("No such member", "noSuchMember");
    }

    if (response.getPassword() == null)
      throw new UnauthorizedException("The member has not password. (signed up with OIDC)",
                                      "noPasswordMember");

    if (!BCryptHashingUtils.verify(dto.getPassword(), response.getPassword())) {
      throw new UnauthorizedException("Invalid password", "invalidPassword");
    }

    MemberInfoResponse memberProfile = response.getMemberInfoResponse();
    return issueAccessTokenAndRefreshToken(profile2JwtTokenPayload(memberProfile));
  }

  public TokenResponse signInWithOIDC(OIDCProvider provider, OAuth2Request dto) {
    OIDCStrategy strategy;

    //TODO: factory
    switch (provider) {
      case KAKAO:
      case GOOGLE:
      default:
        strategy = new GoogleOIDCStrategy(objectMapper, googleOIDCConfig);
    }

    OIDCUserProfile oidcProfile = strategy.authenticate(dto.getCode());
    MemberInfoResponse memberProfile;
    try {
      memberProfile = memberService.getOIDCMemberInfo(provider,
                                                      oidcProfile.getId());
    } catch (NoSuchMemberException e) {
      memberProfile = null;
    }

    // implicit sign up with OIDC
    if (memberProfile == null) {
      memberProfile = memberService.signUpWithOIDC(provider,
                                                   oidcProfile.getId(),
                                                   oidcProfile.getName(),
                                                   oidcProfile.getEmail());
    }

    return issueAccessTokenAndRefreshToken(profile2JwtTokenPayload(memberProfile));
  }

  private JwtTokenPayload profile2JwtTokenPayload(MemberInfoResponse memberProfile) {
    JwtTokenPayload payload = new JwtTokenPayload();
    payload.setSub(memberProfile.getId());
    payload.setEmail(memberProfile.getEmail());
    payload.setName(memberProfile.getName());
    payload.setRole(memberProfile.getRole());
    return payload;
  }
}

