package com._2cha.demo.global.infra.imageupload.exception;

import com._2cha.demo.global.exception.BadRequestException;

public class InvalidImageUrlPathException extends BadRequestException {

  public InvalidImageUrlPathException() {
    super("Image URL path pattern does not matched.", "invalidUrlPath");
  }
}
