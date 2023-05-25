package com._2cha.demo.global.infra.storage.service;

import com._2cha.demo.global.config.S3Config;
import com._2cha.demo.global.infra.storage.exception.InvalidBaseUrlException;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;

@Service
@RequiredArgsConstructor
public class FileStorageService {

  private final S3Client s3Client; //MT-safe checked
  private final S3Config s3Config;

  /**
   * @param filename
   * @param file
   * @return uploaded url
   */
  public String uploadFile(String filename, byte[] file) {
    s3Client.putObject(request -> request.bucket(s3Config.getBucketName())
                                         .key(filename),
                       RequestBody.fromBytes(file));

    return s3Config.getBucketBaseUrl() + filename;
  }

  public String getBaseUrl() {
    return s3Config.getBucketBaseUrl();
  }

  public String extractPath(String url) {
    if (StringUtils.isEmpty(url) || !url.startsWith(s3Config.getBucketBaseUrl())) {
      throw new InvalidBaseUrlException();
    }
    return url.replace(s3Config.getBucketBaseUrl(), "");
  }
}
