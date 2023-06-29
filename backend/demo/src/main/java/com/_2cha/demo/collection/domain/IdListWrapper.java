package com._2cha.demo.collection.domain;

import java.util.List;

/**
 * Wrap list of id due to value bridge only accept one value
 */
public class IdListWrapper {

  public final List<Long> ids;

  public IdListWrapper(List<Long> ids) {
    this.ids = ids;
  }
}