package com._2cha.demo.global.infra.imageupload.exception;

import com._2cha.demo.global.exception.BadRequestException;

public class UnsupportedImageFormatException extends BadRequestException {

  public UnsupportedImageFormatException() {
    super("Unsupported image format.", "unsupportedImageFormat");
  }
}
