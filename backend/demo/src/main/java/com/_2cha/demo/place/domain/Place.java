package com._2cha.demo.place.domain;

import com._2cha.demo.util.GeomUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place {


  @Id
  @GeneratedValue
  @Column(name = "PLACE_ID")
  private Long id;

  /*-----------
   @ Columns
   ----------*/
  //  @Column(nullable = false, unique = true)
  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String address;

  @Column(nullable = false)
  private String lotAddress;

  @Column(nullable = false, columnDefinition = "geometry(point,  4326)") // long, lat
  private Point location;


  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Category category;

  @Lob
  private String thumbnail;
  
  private String site;


  /*-----------
   @ Methods
   ----------*/
  public static Place createPlace(
      String name,
      Category category,
      String address,
      String lotAddress,
      Double longitude,
      Double latitude,
      String thumbnail,
      String site
                                 ) {
    Place place = new Place();
    place.name = name;
    place.category = category;
    place.address = address;
    place.lotAddress = lotAddress;
    place.location = GeomUtils.createPoint(latitude, longitude);
    place.thumbnail = thumbnail;
    place.site = site;
    return place;
  }
}
