package com._2cha.demo.place.dto;

import java.util.List;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class PlaceImageUpdateRequest {

  List<@URL String> images;
}
