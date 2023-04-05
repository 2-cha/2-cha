package com._2cha.demo.review.service;

import com._2cha.demo.review.domain.Tag;
import com._2cha.demo.review.repository.TagRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {

  private final TagRepository tagRepository;

  public List<Tag> getAllTags() {
    return tagRepository.findAll();
  }

  public Tag getTagById(Long id) {
    return tagRepository.findTagById(id);
  }
}
