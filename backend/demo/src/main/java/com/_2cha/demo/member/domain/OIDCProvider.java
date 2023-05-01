package com._2cha.demo.member.domain;


public enum OIDCProvider {
  GOOGLE(Values.GOOGLE), KAKAO(Values.KAKAO);

  public final String value;

  OIDCProvider(String value) {
    this.value = value;
  }

  public static class Values {

    public static final String GOOGLE = "googleOidc";
    public static final String KAKAO = "kakaoOidc";
  }
}
