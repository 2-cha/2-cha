package com._2cha.demo.push.service;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FailedOp {

  String target;
  String code;
}
