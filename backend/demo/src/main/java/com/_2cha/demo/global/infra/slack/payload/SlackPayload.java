package com._2cha.demo.global.infra.slack.payload;

import java.util.List;
import lombok.Data;

@Data
public class SlackPayload {

  private final String channel;
  private final String username;
  List<Block> blocks;
  List<Attachment> attachments;
  private String iconEmoji;

  public SlackPayload(String channel, String username) {
    this.channel = channel;
    this.username = username;
  }

  public void setBlocks(List<Block> blocks) {
    this.blocks = blocks;
  }

  public void setAttachments(List<Attachment> attachments) {
    this.attachments = attachments;
  }

  public void setIconEmoji(String iconEmoji) {
    this.iconEmoji = iconEmoji;
  }
}