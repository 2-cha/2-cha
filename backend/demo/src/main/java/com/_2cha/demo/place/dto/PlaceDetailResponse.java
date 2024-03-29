package com._2cha.demo.place.dto;

import com._2cha.demo.bookmark.dto.BookmarkStatus;
import com._2cha.demo.place.domain.Category;
import com._2cha.demo.review.dto.TagCountResponse;
import com._2cha.demo.util.GeomUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Data;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;

@Data
public class PlaceDetailResponse {

  private Long id;
  private String name;
  private Category category;
  private String address;
  private String lotAddress;
  private String image;
  private String site;
  private Double lat;
  private Double lon;
  private List<TagCountResponse> tags;

  @JsonInclude(Include.NON_NULL)
  private BookmarkStatus bookmarkStatus;

  public PlaceDetailResponse(Long id, String name, Category category, String address,
                             String lotAddress,
                             String imageUrl, String site, Point<G2D> location) {
    this.id = id;
    this.name = name;
    this.category = category;
    this.lotAddress = lotAddress;
    this.address = address;
    this.image = imageUrl;
    this.site = site;
    this.lat = GeomUtils.lat(location);
    this.lon = GeomUtils.lon(location);
  }
}
