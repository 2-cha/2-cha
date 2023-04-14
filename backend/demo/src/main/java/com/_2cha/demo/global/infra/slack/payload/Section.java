package com._2cha.demo.global.infra.slack.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Data;


@Data
@JsonInclude(Include.NON_NULL)
public class Section extends Block {

  private List<Field> fields;
  private Text text;

  public Section() {
    super("section");
  }

  public void addFields(Field... fields) {
    if (this.text != null) {
      throw new IllegalStateException("Cannot have both fields and text");
    }
    if (this.fields == null) {
      this.fields = new ArrayList<>();
    }
    this.fields.addAll(Arrays.stream(fields).toList());
  }
}