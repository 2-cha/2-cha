package com._2cha.demo.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FuzzyMatchingUtils {

  public static String makeFuzzyRegex(String queryText) {
    int i = -1;
    boolean prevSpace = false;
    String queryRegex = "";

    while (++i < queryText.length()) {
      char c = queryText.charAt(i);
      if (HangulUtils.isPartialChar(c)) {
        queryRegex += makeCompleteRange(c);
      } else if (Character.isLetterOrDigit(c)) {
        queryRegex += c;
      } else if (Character.isSpaceChar(c)) {
        prevSpace = true;
      } else {
        continue;
      }
      if (!prevSpace) {
        queryRegex += ".*"; // Fuzzy Matching, to ignore only spaces, use "\\s*".
      }
      prevSpace = false;
    }
    return queryRegex;
  }

  /**
   * NOTE: assume that target always contains pattern.
   * <p>
   * Finds indexes of matching characters of target. It does not include matching range.
   * <p>
   * e.g. 1: param ["ㅇ하세ㅇ", "안녕하세요"] will return [0, 2, 3, 4].
   * <p>
   * e.g. 2: param ["ㅇㅇ", "안녕하세요"] will return [0, 4].
   */
  public static List<Integer> findFuzzyMatchingIndexes(String pattern, String target) {
    if (pattern == null) return Collections.emptyList();
    List<Integer> ret = new ArrayList<>(target.length());

    int nextSearchIdx = 0;
    for (int i = 0; i < pattern.length(); i++) {
      char ch = pattern.charAt(i);
      int matched = HangulUtils.isCho(ch) ?
                    findFirstMatchingInitial(target, ch, nextSearchIdx) :
                    findFirstMatching(target, ch, nextSearchIdx);

      ret.add(matched);
      nextSearchIdx = matched + 1;
    }
    return ret;
  }

  private static String makeCompleteRange(char 초성) {
    char start = HangulUtils.makeCompleteChar(초성, 'ㅏ', '\0');
    char end = HangulUtils.makeCompleteChar(초성, 'ㅣ', 'ㅎ');

    return "[" + start + "-" + end + "]";
  }


  private static int findFirstMatching(String str, char c, int start) {
    start -= 1;
    while (++start < str.length()) {
      char targetChar = str.charAt(start);
      if (Character.isSpaceChar(targetChar)) continue;
      if (targetChar == c) return start;
    }
    return -1;
  }

  private static int findFirstMatchingInitial(String str, char 초성, int start) {
    return IntStream.range(start, str.length())
                    .filter(i -> !Character.isSpaceChar(str.charAt(i)) &&
                                 HangulUtils.isInitialMatches(str.charAt(i), 초성))
                    .findFirst()
                    .orElse(-1);
  }
}
