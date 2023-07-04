package com._2cha.demo.global.infra.imageupload.service;

import com._2cha.demo.global.infra.imageupload.dto.ImageSavedResponse;
import com._2cha.demo.global.infra.imageupload.exception.InvalidImageUrlPathException;
import com._2cha.demo.global.infra.imageupload.exception.NoDetectedExtensionException;
import com._2cha.demo.global.infra.imageupload.exception.UnsupportedImageFormatException;
import com._2cha.demo.global.infra.storage.service.FileStorageService;
import com._2cha.demo.util.ImageUtils;
import io.micrometer.common.util.StringUtils;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
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
   * @param genThumbnail
   * @return uploaded url
   * <p>
   * check actual image extension, and save image as (uuid+detected extension).
   * <p>
   * both original image and generated thumbnail will be saved.
   */
  //TODO: return pair of img and thumbnail
  @Async("imageUploadTaskExecutor")
  public CompletableFuture<ImageSavedResponse> save(byte[] imageBytes, boolean genThumbnail)
      throws IOException {
    String actualExt = ImageUtils.getActualExtension(imageBytes);
    if (actualExt == null) throw new NoDetectedExtensionException();
    String filename = UUID.randomUUID() + actualExt;

    if (genThumbnail) {
      byte[] thumbnailBytes = ImageUtils.resize(imageBytes, THUMB_SIZE, THUMB_SIZE);
      if (thumbnailBytes == null) throw new UnsupportedImageFormatException();
      fileStorageService.uploadFile(IMAGE_PATH + THUMB_PREFIX + filename, thumbnailBytes);
    }

    String url = fileStorageService.uploadFile(IMAGE_PATH + filename, imageBytes);
    return CompletableFuture.completedFuture(new ImageSavedResponse(url));
  }

  @Async("imageUploadTaskExecutor")
  public CompletableFuture<ImageSavedResponse> saveThumbnail(byte[] imageBytes)
      throws IOException {
    String actualExt = ImageUtils.getActualExtension(imageBytes);
    if (actualExt == null) throw new NoDetectedExtensionException();
    String filename = UUID.randomUUID() + actualExt;

    byte[] thumbnailBytes = ImageUtils.resize(imageBytes, THUMB_SIZE, THUMB_SIZE);
    if (thumbnailBytes == null) throw new UnsupportedImageFormatException();
    String url = fileStorageService.uploadFile(IMAGE_PATH + THUMB_PREFIX + filename,
                                               thumbnailBytes);

    return CompletableFuture.completedFuture(new ImageSavedResponse(url));
  }

  public String generateThumbnailPathFromImagePath(String imagePath) {
    if (StringUtils.isEmpty(imagePath) || !imagePath.startsWith(IMAGE_PATH)) {
      throw new InvalidImageUrlPathException();
    }
    return imagePath.replace(IMAGE_PATH, IMAGE_PATH + THUMB_PREFIX);
  }
}
