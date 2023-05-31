package com._2cha.demo.push.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TopicNameValidator implements
                                ConstraintValidator<TopicName, String> {

  @Override
  public void initialize(TopicName constraintAnnotation) {

  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return value == null ||
           (value.length() < 900 && value.matches("[a-zA-Z0-9-_.~%]+"));
  }
}
