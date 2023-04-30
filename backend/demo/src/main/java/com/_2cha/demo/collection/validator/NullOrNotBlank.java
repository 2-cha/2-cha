package com._2cha.demo.collection.validator;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = NullOrNotBlankValidator.class)
public @interface NullOrNotBlank {

  String message() default "{javax.validation.constraints.NotBlank.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
