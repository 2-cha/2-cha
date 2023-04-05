package com._2cha.demo.auth.controller;


import com._2cha.demo.auth.dto.OAuth2Request;
import com._2cha.demo.auth.dto.SignInWithAccountRequest;
import com._2cha.demo.auth.dto.TokenResponse;
import com._2cha.demo.auth.service.AuthService;
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
  public TokenResponse signInWithAccount(@Valid @RequestBody SignInWithAccountRequest dto)
      throws Exception {
    return authService.signInWithAccount(dto);
  }

  @PostMapping(value = "/openid/{provider}/signin")
  public TokenResponse OIDC(@PathVariable OIDCProvider provider,
                            @Valid @RequestBody OAuth2Request dto) throws Exception {

    return authService.signInWithOIDC(provider, dto);
  }
}
