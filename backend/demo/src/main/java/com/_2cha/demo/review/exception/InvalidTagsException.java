package com._2cha.demo.review.exception;

import com._2cha.demo.global.exception.BadRequestException;

public class InvalidTagsException extends BadRequestException {

  public InvalidTagsException() {
    super("Empty tags, or none of the tags are valid.", "invalidTags");
  }
}
