package com._2cha.demo.util;

import jakarta.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.tasks.UnsupportedFormatException;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
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

  @PostConstruct
  private void init() {
    PARSER = parser;
  }
}
