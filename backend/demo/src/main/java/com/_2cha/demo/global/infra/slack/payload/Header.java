package com._2cha.demo.global.infra.slack.payload;

import lombok.Data;

@Data
public class Header extends Block {

  private Text text;

  public Header() {
    super("header");
  }

  public Header(Text text) {
    super("header");
    this.text = text;
  }
}
