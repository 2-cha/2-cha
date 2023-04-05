package com._2cha.demo.review.controller;

import java.util.List;
import lombok.Data;


@Data
public class WriteReviewRequest {

  List<Long> tagIds;
  List<String> imgUrls;
}
