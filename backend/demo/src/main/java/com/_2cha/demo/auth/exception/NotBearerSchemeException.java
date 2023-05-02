package com._2cha.demo.auth.exception;

import com._2cha.demo.global.exception.UnauthorizedException;

public class NotBearerSchemeException extends UnauthorizedException {

  public NotBearerSchemeException() {
    super("Not a Bearer scheme", "notBearerScheme");
  }
}
