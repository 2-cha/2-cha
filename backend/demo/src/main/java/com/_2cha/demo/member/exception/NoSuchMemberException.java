package com._2cha.demo.member.exception;

import com._2cha.demo.global.exception.NotFoundException;

public class NoSuchMemberException extends NotFoundException {

  public NoSuchMemberException() {
    super("No such member", "noSuchMember");
  }
}
