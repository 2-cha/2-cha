package com._2cha.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthcheckController {

  @GetMapping("/")
  public String healthchecker() {
    return "OK";
  }
}
