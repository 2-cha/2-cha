package com._2cha.demo.review.service;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import com._2cha.demo.global.exception.BadRequestException;
import com._2cha.demo.member.domain.Member;
import com._2cha.demo.member.exception.NoSuchMemberException;
import com._2cha.demo.member.service.MemberService;
import com._2cha.demo.review.domain.Category;
import com._2cha.demo.review.domain.Tag;
import com._2cha.demo.review.domain.TagCreationRequest;
import com._2cha.demo.review.dto.MakeTagReqResponse;
import com._2cha.demo.review.dto.TagCreationReqBriefResponse;
import com._2cha.demo.review.dto.TagCreationReqDetailResponse;
import com._2cha.demo.review.dto.TagReqAcceptedResponse;
import com._2cha.demo.review.dto.TagReqUpdatedResponse;
import com._2cha.demo.review.dto.TagSearchResponse;
import com._2cha.demo.review.dto.TagWithoutCategoryResponse;
import com._2cha.demo.review.exception.NoSuchTagCreationReqException;
import com._2cha.demo.review.repository.TagCreationRequestRepository;
import com._2cha.demo.review.repository.TagRepository;
import com._2cha.demo.util.FuzzyMatchingUtils;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

  private final TagRepository tagRepository;
  private final TagCreationRequestRepository tagReqRepository;
  private final MemberService memberService;

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
  public List<TagSearchResponse> fuzzySearchTagsByHangul(String queryText) {
    List<Tag> tags;
    if (queryText == null) {
      tags = tagRepository.findAll();
    } else {
      String queryRegex = FuzzyMatchingUtils.makeFuzzyRegex(queryText);
      tags = tagRepository.findTagsByMsgMatchesRegex(queryRegex);
    }
    return tags.stream().map(TagSearchResponse::new).toList();
  }

  public Map<Category, List<TagWithoutCategoryResponse>> fuzzySearchCategorizedTagsByHangul(
      String queryText) {
    List<Tag> tags;
    if (queryText == null) {
      tags = tagRepository.findAll();
    } else {
      String queryRegex = FuzzyMatchingUtils.makeFuzzyRegex(queryText);
      tags = tagRepository.findTagsByMsgMatchesRegex(queryRegex);
    }
    return tags.stream().collect(Collectors.groupingBy(Tag::getCategory,
                                                       mapping(TagWithoutCategoryResponse::new,
                                                               toList())));
  }

  public List<TagCreationReqBriefResponse> getAllTagCreationRequests(Pageable pageParam) {
    List<TagCreationRequest> requests = tagReqRepository.findAllByOrderByRequestedAtDesc(
        pageParam);

    return requests.stream()
                   .map(TagCreationReqBriefResponse::new)
                   .toList();
  }

  public TagCreationReqDetailResponse getTagCreationRequest(Long tagReqId) {
    Optional<TagCreationRequest> tr = tagReqRepository.findById(tagReqId);
    if (tr.isEmpty()) throw new NoSuchTagCreationReqException();

    return new TagCreationReqDetailResponse(tr.get());
  }


  /*-----------
   @ Commands
   ----------*/
  @Transactional
  public MakeTagReqResponse makeTagCreationRequest(Long requesterId, String emoji, String message) {
    Member requester = memberService.findById(requesterId);
    if (requester == null) throw new NoSuchMemberException();

    TagCreationRequest tagReq;
    try {
      tagReq = TagCreationRequest.makeRequest(requester, emoji, message);
    } catch (IllegalArgumentException e) {
      throw new BadRequestException(e.getMessage());
    }

    tagReqRepository.save(tagReq);

    return new MakeTagReqResponse(tagReq);
  }

  @Transactional
  public TagReqUpdatedResponse updateTagCreationRequest(Long tagReqId, Category category,
                                                        String emoji,
                                                        String message) {
    Optional<TagCreationRequest> tr = tagReqRepository.findById(tagReqId);
    if (tr.isEmpty()) throw new NoSuchTagCreationReqException();
    TagCreationRequest tagReq = tr.get();
    tagReq.setCategory(category);
    tagReq.modifyEmoji(emoji);
    tagReq.modifyMessage(message);
    tagReqRepository.save(tagReq);

    return new TagReqUpdatedResponse(tagReq);
  }

  @Transactional
  public TagReqAcceptedResponse acceptTagCreationRequest(Long tagReqId) {
    Optional<TagCreationRequest> tr = tagReqRepository.findById(tagReqId);
    if (tr.isEmpty()) throw new NoSuchTagCreationReqException();
    TagCreationRequest tagReq = tr.get();

    Tag tag;
    try {
      tag = Tag.createTag(tagReq);
    } catch (IllegalArgumentException e) {
      throw new BadRequestException(e.getMessage());
    }

    //tagReq.getRequester();  // May notify to requester
    tagRepository.save(tag);
    tagReqRepository.delete(tagReq);

    return new TagReqAcceptedResponse(tagReq, tag);
  }

  @Transactional
  public void rejectTagCreationRequest(Long tagReqId) {
    Optional<TagCreationRequest> tr = tagReqRepository.findById(tagReqId);
    if (tr.isEmpty()) throw new NoSuchTagCreationReqException();
    TagCreationRequest tagReq = tr.get();

    //tagReq.getRequester();  // May notify to requester
    tagReqRepository.delete(tagReq);
  }
}
