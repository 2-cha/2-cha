package com._2cha.demo.auth.controller;


import com._2cha.demo.auth.dto.JwtRefreshRequest;
import com._2cha.demo.auth.dto.OIDCRequest;
import com._2cha.demo.auth.dto.SignInWithAccountRequest;
import com._2cha.demo.auth.dto.TokenResponse;
import com._2cha.demo.auth.service.AuthService;
import com._2cha.demo.global.annotation.Auth;
import com._2cha.demo.global.annotation.Authed;
import com._2cha.demo.member.domain.OIDCProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/signin")
  public TokenResponse signInWithAccount(@Valid @RequestBody SignInWithAccountRequest dto) {
    return authService.signInWithAccount(dto.getEmail(), dto.getPassword());
  }

  @PostMapping("/refresh")
  public TokenResponse refreshJwt(@Valid @RequestBody JwtRefreshRequest dto) {
    return authService.refreshJwt(dto.getRefreshToken());
  }

  @PostMapping(value = "/openid/{provider}/signin")
  public TokenResponse signInWithOIDC(@PathVariable OIDCProvider provider,
                                      @Valid @RequestBody OIDCRequest dto) {

    return authService.signInWithOIDC(provider, dto.getCode());
  }

  @Auth
  @PostMapping(value = "/signout")
  public void signOut(@Valid @RequestBody JwtRefreshRequest dto) {
    authService.signOut(dto.getRefreshToken());
  }

  @Auth
  @PostMapping(value = "/signout/all")
  public void signOutAll(@Authed Long memberId) {
    authService.signOutAll(memberId);
  }
}
