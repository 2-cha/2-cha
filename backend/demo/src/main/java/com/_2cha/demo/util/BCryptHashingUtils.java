package com._2cha.demo.util;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptHashingUtils {

  private static final int WORKLOAD = 10;

  public static String hash(String plain) {
    String salt = BCrypt.gensalt(WORKLOAD);
    return BCrypt.hashpw(plain, salt);
  }

  public static boolean verify(String plain, String hashed) {
    return BCrypt.checkpw(plain, hashed);
  }
}
