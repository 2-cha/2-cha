package com._2cha.demo.global.infra.slack.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class Text {

  private TextType type;
  private String text;

  public Text(TextType type, String text) {
    this.type = type;
    this.text = text;
  }

  @Getter
  public enum TextType {
    @JsonProperty("plain_text")
    PLAIN,
    @JsonProperty("mrkdwn")
    MARKDOWN;
  }
}
