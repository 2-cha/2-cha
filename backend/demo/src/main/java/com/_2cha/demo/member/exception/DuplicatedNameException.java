package com._2cha.demo.member.exception;


import com._2cha.demo.global.exception.ConflictException;

public class DuplicatedNameException extends ConflictException {

  public DuplicatedNameException() {
    super("Submitted name is duplicated.", "duplicatedName");
  }
}
