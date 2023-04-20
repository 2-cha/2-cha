package com._2cha.demo.auth;

import com._2cha.demo.TempController;
import com._2cha.demo.auth.controller.AuthController;
import com._2cha.demo.auth.dto.JwtTokenPayload;
import com._2cha.demo.auth.dto.SignInWithAccountRequest;
import com._2cha.demo.auth.dto.TokenResponse;
import com._2cha.demo.auth.service.AuthService;
import com._2cha.demo.global.exception.NoSuchMemberException;
import com._2cha.demo.global.exception.UnauthorizedException;
import com._2cha.demo.member.controller.MemberController;
import com._2cha.demo.member.dto.SignUpRequest;
import java.io.IOException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles({"test"})
class AuthTests {

  @Autowired
  AuthController authController;
  @Autowired
  AuthService authService;
  @Autowired
  MemberController memberController;
  @Autowired
  TempController tempController;

  @BeforeEach
  void mockUp() throws Exception {
    SignUpRequest request = new SignUpRequest();
    request.setName("admin");
    request.setEmail("admin@2cha.com");
    request.setPassword("1234");
    memberController.signUp(request);
  }

  @Test
  void signIn() throws Exception {
    SignInWithAccountRequest request = new SignInWithAccountRequest();
    request.setEmail("admin@2cha.com");
    request.setPassword("1234");

    TokenResponse response = authController.signInWithAccount(request);
    Assertions.assertThat(authService.verifyJwt(response.getAccessToken()))
              .isInstanceOf(JwtTokenPayload.class);
  }

  @Test
  void signIn_Fail() {
    SignInWithAccountRequest request = new SignInWithAccountRequest();
    request.setEmail("admin@2cha.com");
    request.setPassword("1235");

    Assertions.assertThatThrownBy(() -> authController.signInWithAccount(request))
              .isInstanceOf(UnauthorizedException.class);

    request.setEmail("dummy@2cha.com");
    Assertions.assertThatThrownBy(() -> authController.signInWithAccount(request))
              .isInstanceOf(NoSuchMemberException.class);
  }

  @Test
  void OIDC() throws IOException {
    //TODO
  }
}