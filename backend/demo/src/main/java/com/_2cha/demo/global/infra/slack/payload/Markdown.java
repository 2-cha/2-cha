package com._2cha.demo.global.infra.slack.payload;

import lombok.Data;

@Data
public class Markdown extends Text {

  public Markdown(String text) {
    super(TextType.MARKDOWN, text);
  }
}
