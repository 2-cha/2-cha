package com._2cha.demo.global.infra.slack.payload;

import lombok.Data;

@Data
public class Divider extends Block {

  public Divider() {
    super("divider");
  }
}