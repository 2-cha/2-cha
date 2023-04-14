package com._2cha.demo.global.infra.slack.payload;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class Block {

  private String type;

  public Block(String type) {
    this.type = type;
  }

  public static List<Block> build(Block... blocks) {
    List<Block> list = new ArrayList<>();
    list.addAll(Arrays.stream(blocks).toList());
    return list;
  }
}


