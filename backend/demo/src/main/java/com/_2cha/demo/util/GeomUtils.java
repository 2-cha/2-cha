package com._2cha.demo.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GeomUtils {

  private static GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

  public static Point createPoint(Double latitude, Double longitude) {
    return geometryFactory.createPoint(new Coordinate(longitude, latitude));
  }

  public static Double calcDistance(Double lat1, Double lon1, Double lat2, Double lon2, char unit) {
    Double theta = lon1 - lon2;
    Double dist =
        Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(
            deg2rad(lat2)) * Math.cos(deg2rad(theta));
    dist = Math.acos(dist);
    dist = rad2deg(dist);
    dist = dist * 60 * 1.1515;
    if (unit == 'K') {
      dist = dist * 1.609344;
    } else if (unit == 'M') {
      dist = dist * 0.0011609344;
    }
    return (dist);
  }

  private static Double deg2rad(Double deg) {
    return (deg * Math.PI / 180.0);
  }

  private static Double rad2deg(Double rad) {
    return (rad * 180.0 / Math.PI);
  }
}
