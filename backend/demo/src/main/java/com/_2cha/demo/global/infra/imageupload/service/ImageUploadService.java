package com._2cha.demo.global.infra.imageupload.service;

import com._2cha.demo.global.infra.imageupload.dto.ImageSavedResponse;
import com._2cha.demo.global.infra.imageupload.exception.NoDetectedExtensionException;
import com._2cha.demo.global.infra.imageupload.exception.UnsupportedImageFormatException;
import com._2cha.demo.global.infra.storage.service.FileStorageService;
import com._2cha.demo.util.ImageUtils;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

  private final FileStorageService fileStorageService;

  private static final int THUMB_SIZE = 200;
  private static final String THUMB_PREFIX = "thumb_";
  private static final String IMAGE_PATH = "images/";

  /**
   * @param imageBytes
   * @return uploaded url
   * <p>
   * check actual image extension, and save image as (uuid+detected extension).
   * <p>
   * both original image and generated thumbnail will be saved.
   */
  public ImageSavedResponse save(byte[] imageBytes) throws IOException {
    String actualExt = ImageUtils.getActualExtension(imageBytes);
    if (actualExt == null) throw new NoDetectedExtensionException();

    byte[] thumbnailBytes = ImageUtils.resize(imageBytes, THUMB_SIZE, THUMB_SIZE);
    if (thumbnailBytes == null) throw new UnsupportedImageFormatException();

    String filename = UUID.randomUUID() + actualExt;
    fileStorageService.uploadFile(IMAGE_PATH + THUMB_PREFIX + filename, thumbnailBytes);
    String url = fileStorageService.uploadFile(IMAGE_PATH + filename, imageBytes);
    return new ImageSavedResponse(url);
  }
}
