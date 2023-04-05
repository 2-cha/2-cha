package com._2cha.demo.global.exception;

public class NoPrivilegeException extends
                                  ForbiddenException {

  public NoPrivilegeException() {
    super("No privilege", "noPrivilege");
  }

  public NoPrivilegeException(String required) {
    super("Require " + required + " privilege", "noPrivilege");
  }
}
