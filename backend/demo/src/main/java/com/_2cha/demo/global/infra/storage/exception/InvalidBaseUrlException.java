package com._2cha.demo.global.infra.storage.exception;

import com._2cha.demo.global.exception.BadRequestException;

public class InvalidBaseUrlException extends
                                     BadRequestException {

  public InvalidBaseUrlException() {
    super("Base URL does not matched with storage's.", "invalidBaseUrl");
  }
}
