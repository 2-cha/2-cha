package com._2cha.demo.global.validator.imagemime;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * validate Content-type only. No check for actual file content.
 */
@Component
public class ImageMimeValidator implements
                                ConstraintValidator<ImageMime, MultipartFile> {

  @Override
  public void initialize(ImageMime constraintAnnotation) {}

  @Override
  public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
    //TODO: queryParamNotValid --> invalidValue에 Multifile instance hash값이 들어가버림
    return supports(file.getContentType());
  }

  private boolean supports(String contentType) {
    if (contentType == null) return false;
    return contentType.startsWith("image/");
  }
}
