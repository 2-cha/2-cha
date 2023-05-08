package com._2cha.demo.global.infra.imageupload.exception;

import com._2cha.demo.global.exception.BadRequestException;

public class NoDetectedExtensionException extends BadRequestException {

  public NoDetectedExtensionException() {
    super("No matching extension for submitted file. Check if file is valid.",
          "noDetectedExtension");
  }
}
