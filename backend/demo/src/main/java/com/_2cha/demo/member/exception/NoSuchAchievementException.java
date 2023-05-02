package com._2cha.demo.member.exception;

import com._2cha.demo.global.exception.NotFoundException;

public class NoSuchAchievementException extends NotFoundException {

  public NoSuchAchievementException() {
    super("No such achievement", "noSuchAchievement");
  }
}
