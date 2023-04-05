package com._2cha.demo.global.exception;

public class NotBearerSchemeException extends UnauthorizedException {

  public NotBearerSchemeException() {
    super("Not a Bearer scheme", "notBearerScheme");
  }
}
