package com._2cha.demo.global.infra.slack.payload;

import com._2cha.demo.global.infra.slack.payload.Text.TextType;
import lombok.Data;

@Data
public class Field {

  private TextType type;
  private String text;

  public Field(TextType type, String text) {
    this.type = type;
    this.text = text;
  }
}