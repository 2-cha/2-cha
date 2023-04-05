package com._2cha.demo.global.annotation;

import com._2cha.demo.member.domain.Role;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth {


  @AliasFor("role")
  Role value() default Role.MEMBER;

  @AliasFor("value")
  Role role() default Role.MEMBER;
}
