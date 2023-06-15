package com._2cha.demo.util;

public class HangulUtils {

  static String 초성 = "ㄱㄲㄴㄷㄸㄹㅁㅂㅃㅅㅆㅇㅈㅉㅊㅋㅌㅍㅎ";
  static String 중성 = "ㅏㅐㅑㅒㅓㅔㅕㅖㅗㅘㅙㅚㅛㅜㅝㅞㅟㅠㅡㅢㅣ";
  static String 종성 = "\0ㄱㄲㄳㄴㄵㄶㄷㄹㄺㄻㄼㄽㄾㄿㅀㅁㅂㅄㅅㅆㅇㅈㅊㅋㅌㅍㅎ";

  /*------------------------
   @ (초성 * 21 * 28) + (중성 * 28) + 종성 + 0xAC00(가)
   ------------------------*/

  public static boolean isCho(char c) {
    if (초성.contains(String.valueOf(c))) {
      return true;
    }
    return false;
  }

  public static boolean isCompleteChar(char c) {
    return '가' <= c && c <= '힣';
  }

  public static boolean isPartialChar(char c) {
    return 'ㄱ' <= c && c <= 'ㅣ';
  }

  public static char makeCompleteChar(char 초, char 중, char 종) {
    return (char) ('가' + (초성.indexOf(초) * 21 * 28) + (중성.indexOf(중) * 28) + 종성.indexOf(종));
  }

  public static boolean isInitialMatches(char 글자, char 초성) {
    if (isCompleteChar(초성)) throw new IllegalArgumentException("No 초성");
    if (isCompleteChar(글자)) return false;

    char start = makeCompleteChar(초성, 'ㅏ', '\0');
    char end = makeCompleteChar(초성, 'ㅣ', 'ㅎ');
    return (start <= 글자 && 글자 <= end);
  }
}