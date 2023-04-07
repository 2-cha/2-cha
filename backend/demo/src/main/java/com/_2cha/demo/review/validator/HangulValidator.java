package com._2cha.demo.review.validator;

import com._2cha.demo.review.util.HangulUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HangulValidator implements
                             ConstraintValidator<Hangul, String> {

  @Override
  public void initialize(Hangul constraintAnnotation) {
    
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return value.chars().allMatch(c -> Character.isSpaceChar(c) ||
                                       HangulUtils.isPartialChar((char) c) ||
                                       HangulUtils.isCompleteChar((char) c));
  }
}
