package com._2cha.demo.global.infra.slack.payload;

import lombok.Data;

@Data
public class PlainText extends Text {

  private boolean emoji = true;

  public PlainText(String text) {
    super(TextType.PLAIN, text);
  }
}
