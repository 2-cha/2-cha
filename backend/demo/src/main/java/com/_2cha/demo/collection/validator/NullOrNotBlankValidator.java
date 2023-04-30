package com._2cha.demo.collection.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NullOrNotBlankValidator implements ConstraintValidator<NullOrNotBlank, String> {

  public void initialize(NullOrNotBlank parameters) {
    // Nothing to do here
  }

  public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
    return value == null || value.trim().length() > 0;
  }
}