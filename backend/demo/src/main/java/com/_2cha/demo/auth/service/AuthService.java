package com._2cha.demo.auth.service;

import com._2cha.demo.auth.config.GoogleOIDCConfig;
import com._2cha.demo.auth.config.JwtConfig;
import com._2cha.demo.auth.dto.JwtAccessTokenPayload;
import com._2cha.demo.auth.dto.JwtRefreshTokenPayload;
import com._2cha.demo.auth.dto.JwtTokenPayload;
import com._2cha.demo.auth.dto.OIDCUserProfile;
import com._2cha.demo.auth.dto.TokenResponse;
import com._2cha.demo.auth.repository.RefreshToken;
import com._2cha.demo.auth.repository.TokenRepository;
import com._2cha.demo.auth.strategy.oidc.OIDCStrategy;
import com._2cha.demo.global.exception.UnauthorizedException;
import com._2cha.demo.member.domain.OIDCProvider;
import com._2cha.demo.member.dto.MemberCredResponse;
import com._2cha.demo.member.dto.MemberInfoResponse;
import com._2cha.demo.member.exception.NoSuchMemberException;
import com._2cha.demo.member.service.MemberService;
import com._2cha.demo.util.BCryptHashingUtils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Field;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

  private final JwtConfig jwtConfig;
  private final GoogleOIDCConfig googleOIDCConfig;

  private final ObjectMapper objectMapper;  //thread-safe
  private final MemberService memberService;

  private final Map<String, OIDCStrategy> oidcStrategyMap;
  private final TokenRepository tokenRepository;
  private final TransactionTemplate tx;

  public <T extends JwtTokenPayload> T verifyJwt(String token, Class<T> payloadType) {
    String key;
    if (payloadType.isAssignableFrom(JwtAccessTokenPayload.class)) {
      key = jwtConfig.getAccessKey();
    } else if (payloadType.isAssignableFrom(JwtRefreshTokenPayload.class)) {
      key = jwtConfig.getRefreshKey();
    } else {
      throw new UnsupportedOperationException("Unsupported payload type.");
    }
    Verification verification = JWT.require(Algorithm.HMAC512(key));

    Field[] claims = payloadType.getDeclaredFields();

    Arrays.stream(claims).
          forEach(claim -> verification.withClaimPresence(claim.getName()));

    JWTVerifier verifier = verification
        .withIssuer(jwtConfig.getIssuer())
        .build();
    DecodedJWT jwt = verifier.verify(token);  // not decoded..!

    String payload = jwt.getPayload();
    String decodedPayload = new String(Base64.getUrlDecoder().decode(payload));

    try {
      return objectMapper.readValue(decodedPayload, payloadType);
    } catch (JsonProcessingException e) {
      throw new UnauthorizedException("Invalid payload format", "invalidJwtPayload");
    }
  }

  @Transactional(readOnly = true)
  public TokenResponse refreshJwt(String refreshToken) {
    JwtTokenPayload payload = verifyJwt(refreshToken, JwtRefreshTokenPayload.class);
    Long memberId = payload.getSub();
    RefreshToken stored = tokenRepository.findById(memberId);
    if (stored == null) throw new UnauthorizedException("Cannot find such token");

    List<String> storedValues = stored.getValues();
    if (!storedValues.contains(refreshToken)) {
      throw new UnauthorizedException("Token not matched");
    }

    MemberInfoResponse memberInfo = memberService.getMemberInfoById(payload.getSub());
    return new TokenResponse(issueJwt(info2AccessTokenPayload(memberInfo),
                                      jwtConfig.getAccessKey(),
                                      jwtConfig.getAccessLifetime()),
                             refreshToken
    );
  }

  public TokenResponse signInWithAccount(String email, String password) {
    MemberCredResponse response;
    try {
      response = memberService.getMemberCredByEmail(email);
    } catch (NoSuchMemberException e) {
      throw new UnauthorizedException("No such member", "noSuchMember");
    }

    if (response.getPassword() == null) {
      throw new UnauthorizedException("The member has not password. (signed up with OIDC)",
                                      "noPasswordMember");
    }

    if (!BCryptHashingUtils.verify(password, response.getPassword())) {
      throw new UnauthorizedException("Invalid password", "invalidPassword");
    }

    MemberInfoResponse memberInfo = new MemberInfoResponse(response.getId(),
                                                           response.getEmail(),
                                                           response.getName(),
                                                           response.getRole());

    return issueAccessTokenAndRefreshToken(info2AccessTokenPayload(memberInfo));
  }

  public void signOut(String refreshToken) {
    JwtTokenPayload payload = verifyJwt(refreshToken, JwtRefreshTokenPayload.class);
    Long memberId = payload.getSub();
    RefreshToken stored = tokenRepository.findById(memberId);
    if (stored == null) throw new UnauthorizedException("Cannot find such token");

    List<String> storedValues = stored.getValues();
    if (!storedValues.contains(refreshToken)) {
      throw new UnauthorizedException("Token not matched");
    }

    tx.executeWithoutResult(status -> {
      if (storedValues.size() == 1) {
        tokenRepository.delete(stored);
      } else {
        storedValues.remove(refreshToken);
        tokenRepository.save(stored);
      }
    });
  }

  public void signOutAll(Long memberId) {
    RefreshToken stored = tokenRepository.findById(memberId);
    if (stored == null) throw new UnauthorizedException("Cannot find any token");

    tx.executeWithoutResult(status -> tokenRepository.delete(stored));
  }

  public TokenResponse signInWithOIDC(OIDCProvider provider, String authCode) {
    OIDCStrategy strategy = oidcStrategyMap.get(provider.value);
    OIDCUserProfile oidcProfile = strategy.authenticate(authCode);
    MemberInfoResponse memberInfo;
    try {
      memberInfo = memberService.getMemberInfoByOidcId(provider,
                                                       oidcProfile.getId());
    } catch (NoSuchMemberException e) {
      memberInfo = null;
    }

    // implicit sign up with OIDC
    if (memberInfo == null) {
      memberInfo = memberService.signUpWithOIDC(provider,
                                                oidcProfile.getId(),
                                                oidcProfile.getName(),
                                                oidcProfile.getEmail(),
                                                oidcProfile.getImageUrl());
    }

    return issueAccessTokenAndRefreshToken(info2AccessTokenPayload(memberInfo));
  }

  private JwtTokenPayload info2AccessTokenPayload(MemberInfoResponse memberInfo) {
    return new JwtAccessTokenPayload(
        memberInfo.getId(),
        memberInfo.getEmail(),
        memberInfo.getName(),
        memberInfo.getRole()
    );
  }

  private TokenResponse issueAccessTokenAndRefreshToken(JwtTokenPayload payload) {
    JwtRefreshTokenPayload refreshTokenPayload = new JwtRefreshTokenPayload();
    refreshTokenPayload.setSub(payload.getSub());

    String accessToken = issueJwt(payload, jwtConfig.getAccessKey(), jwtConfig.getAccessLifetime());
    String refreshToken = issueJwt(refreshTokenPayload, jwtConfig.getRefreshKey(),
                                   jwtConfig.getRefreshLifetime());

    tx.executeWithoutResult(status -> {
      RefreshToken stored = tokenRepository.findById(payload.getSub());
      if (stored != null) {
        stored.addToken(refreshToken);
        tokenRepository.save(stored);
      } else {
        tokenRepository.save(new RefreshToken(payload.getSub(), List.of(refreshToken)));
      }
    });

    return new TokenResponse(accessToken, refreshToken);
  }


  private String issueJwt(JwtTokenPayload payload, String key, Integer lifetime) {
    return JWT.create()
              .withPayload(objectMapper.convertValue(payload, Map.class))
              .withIssuer(jwtConfig.getIssuer())
              .withSubject(payload.getSub().toString())
              .withExpiresAt(Instant.now().plus(lifetime, ChronoUnit.MINUTES))
              .sign(Algorithm.HMAC512(key));
  }
}

