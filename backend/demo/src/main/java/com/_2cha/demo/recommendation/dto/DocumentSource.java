package com._2cha.demo.recommendation.dto;

import java.util.Map;


public interface DocumentSource {

  Map<String, String> getFields();

  Long getId();
}
