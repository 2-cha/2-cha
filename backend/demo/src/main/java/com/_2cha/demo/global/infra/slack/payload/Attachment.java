package com._2cha.demo.global.infra.slack.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Attachment {


  @JsonProperty("mrkdwn_in")
  private List<String> markdownIn = new ArrayList<>();
  private String color;
  private String title;
  private String text;


  public void setColor(String color) {
    this.color = color;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setText(String text) {
    this.text = text;
  }

  public void addMarkdownIn(String property) {
    this.markdownIn.add(property);
  }
}