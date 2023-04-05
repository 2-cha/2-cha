package com._2cha.demo.global.exception;

public class NoSuchMemberException extends NotFoundException {

  public NoSuchMemberException() {
    super("No Such Member", "noSuchMember");
  }
}
