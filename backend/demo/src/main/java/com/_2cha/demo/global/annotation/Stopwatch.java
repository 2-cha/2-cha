package com._2cha.demo.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Stopwatch {

  @AliasFor("unit")
  Unit value() default Unit.MILLISECONDS;

  @AliasFor("value")
  Unit unit() default Unit.MILLISECONDS;

  enum Unit {
    SECONDS, MILLISECONDS, NANOSECONDS
  }
}
