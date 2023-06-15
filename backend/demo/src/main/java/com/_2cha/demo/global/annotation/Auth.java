package com._2cha.demo.global.annotation;

import com._2cha.demo.member.domain.Role;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

/**
 * If the token exists, it will be validated and `memberId` will be injected to @Authed argument.
 * <p>
 * Note that @Auth(GUEST) will pass request even if token is not included.
 * <p>
 * So, @Authed argument will be null.
 * <p>
 * But if the token exists, the memberId will be provided even if it is a guest privilege handler.
 * <p>
 * <p>
 * <p>
 * It allows higher privilege. e.g.) @Auth(GUEST) allows MEMBER, ADMIN.
 * <p>
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth {


  @AliasFor("role")
  Role value() default Role.MEMBER;

  @AliasFor("value")
  Role role() default Role.MEMBER;
}
