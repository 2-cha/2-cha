package com._2cha.demo.util;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import jakarta.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.tasks.UnsupportedFormatException;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageUtils {

  private final Tika parser;   // MT-safe checked
  private static final MimeTypes ALL_MIMES = MimeTypes.getDefaultMimeTypes();
  private static Tika PARSER;

  public static byte[] resize(byte[] imageBytes, int targetWidth, int targetHeight)
      throws IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      Thumbnails.of(bais)
                .size(targetWidth, targetHeight)
                .toOutputStream(baos);
    } catch (UnsupportedFormatException e) {
      return null;
    }
    return baos.toByteArray();
  }

  public static String getActualExtension(byte[] imageBytes) {
    String actualMime = PARSER.detect(imageBytes);
    String actualExt;
    try {
      actualExt = ALL_MIMES.forName(actualMime).getExtension();
    } catch (MimeTypeException e) {
      actualExt = null;
    }
    return actualExt;
  }

  public static Point getGeoPoint(byte[] imageBytes) {
    try (InputStream inputStream = new ByteArrayInputStream(imageBytes)) {
      Metadata metadata = ImageMetadataReader.readMetadata(inputStream);
      GeoLocation geoLocation;
      GpsDirectory gps = metadata.getFirstDirectoryOfType(GpsDirectory.class);
      if (gps != null && (geoLocation = gps.getGeoLocation()) != null) {
        return GeomUtils.createPoint(geoLocation.getLatitude(), geoLocation.getLongitude());
      }
    } catch (ImageProcessingException | IOException ignored) {;}
    return null;
  }

  @PostConstruct
  private void init() {
    PARSER = parser;
  }
}
