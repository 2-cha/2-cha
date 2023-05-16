package com._2cha.demo.review.service;

import com._2cha.demo.review.domain.Tag;
import com._2cha.demo.review.dto.TagWithIdResponse;
import com._2cha.demo.review.repository.TagRepository;
import com._2cha.demo.util.HangulUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {

  private final TagRepository tagRepository;

  public List<Tag> findAll() {
    return tagRepository.findAll();
  }

  public Tag findById(Long id) {
    return tagRepository.findTagById(id);
  }

  public List<Tag> findTagsByIdIn(List<Long> ids) {
    return tagRepository.findTagsByIdIn(ids);
  }

  /*-----------
   @ Queries
   ----------*/
  public List<TagWithIdResponse> fuzzySearchTagsByHangul(String queryText) {
    String queryRegex = this.makeQueryRegex(queryText);
    List<Tag> tags = tagRepository.findTagsByMsgMatchesRegex(queryRegex);

    return tags.stream().map(TagWithIdResponse::new).toList();
  }


  private String makeQueryRegex(String queryText) {

    int i = -1;
    boolean prevSpace = false;
    String queryRegex = "";

    while (++i < queryText.length()) {
      char c = queryText.charAt(i);
      if (HangulUtils.isCompleteChar(c)) {
        queryRegex += c;
      } else if (HangulUtils.isPartialChar(c)) {
        queryRegex += makeCompleteRange(c);
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

  private String makeCompleteRange(char 초성) {
    char start = HangulUtils.makeCompleteChar(초성, 'ㅏ', '\0');
    char end = HangulUtils.makeCompleteChar(초성, 'ㅣ', 'ㅎ');

    return "[" + start + "-" + end + "]";
  }
}
