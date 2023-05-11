package com._2cha.demo.global.validator.imagemime;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageMimeValidator.class)
public @interface ImageMime {

  String message() default "Invalid MIME type. (allow image type only)";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  String value() default "";
}
