package com._2cha.demo.review.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HangulValidator.class)
public @interface Hangul {

  String message() default "Parameter only allows Hangul(한글)";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  String value() default "";
}
