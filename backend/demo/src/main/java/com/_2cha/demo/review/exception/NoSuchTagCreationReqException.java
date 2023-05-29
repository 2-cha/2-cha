package com._2cha.demo.review.exception;

import com._2cha.demo.global.exception.BadRequestException;

public class NoSuchTagCreationReqException extends BadRequestException {

  public NoSuchTagCreationReqException() {
    super("No tag creation request with such id.", "noSuchTagReq");
  }
}
