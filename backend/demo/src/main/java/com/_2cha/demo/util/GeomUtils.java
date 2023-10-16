package com._2cha.demo.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Geometries;
import org.geolatte.geom.Point;
import org.geolatte.geom.crs.CoordinateReferenceSystems;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GeomUtils {

  public static Point<G2D> createPoint(Double latitude, Double longitude) {
    return Geometries.mkPoint(new G2D(longitude, latitude),
                              CoordinateReferenceSystems.WGS84);
  }

  public static Double lat(Point<G2D> point) {return point.getPosition().getCoordinate(1);}

  public static Double lon(Point<G2D> point) {return point.getPosition().getCoordinate(0);}

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
