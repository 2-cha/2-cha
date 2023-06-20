package com._2cha.demo.collection.service;

import com._2cha.demo.collection.domain.Collection;
import com._2cha.demo.collection.domain.CollectionLike;
import com._2cha.demo.collection.exception.NoSuchCollectionException;
import com._2cha.demo.collection.repository.CollectionLikeRepository;
import com._2cha.demo.collection.repository.CollectionRepository;
import com._2cha.demo.member.domain.Member;
import com._2cha.demo.member.service.MemberService;
import com._2cha.demo.review.dto.LikeStatus;
import com._2cha.demo.review.exception.AlreadyLikedException;
import com._2cha.demo.review.exception.NotLikedException;
import jakarta.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//TODO: 도메인 분리
@Service
@Transactional
@RequiredArgsConstructor
public class CollectionLikeService {

  private final CollectionLikeRepository likeRepository;
  private final CollectionRepository collectionRepository;
  private final MemberService memberService;

  /*-----------
   @ Commands
   ----------*/

  public void likeCollection(Long memberId, Long collId) {
    CollectionLike like = likeRepository.findByMemberIdAndCollectionId(memberId, collId);
    if (like != null) throw new AlreadyLikedException();

    Member member = memberService.findById(memberId);
    Collection collection = collectionRepository.findCollectionById(collId);
    if (collection == null) throw new NoSuchCollectionException();

    like = CollectionLike.createLike(member, collection);

    likeRepository.save(like);
  }

  public void unlikeCollection(Long memberId, Long collId) {
    CollectionLike like = likeRepository.findByMemberIdAndCollectionId(memberId, collId);
    if (like == null) throw new NotLikedException();

    likeRepository.delete(like);
  }

  /*-----------
   @ Queries
   ----------*/
  public LikeStatus getLikeStatus(@Nullable Long memberId, Long collId) {
    return new LikeStatus(isLikedCollection(memberId, collId), getCount(collId));
  }

  public Map<Long, LikeStatus> getLikeStatus(@Nullable Long memberId, List<Long> collIds) {
    Map<Long, LikeStatus> likeStatusMap = new HashMap<>();

    Map<Long, Long> countMap = getCount(collIds);
    Map<Long, Boolean> likedMap = areLikedCollection(memberId, collIds);

    collIds.forEach(collId -> likeStatusMap.put(collId, new LikeStatus(likedMap.get(collId),
                                                                       countMap.get(collId))));
    return likeStatusMap;
  }

  private Long getCount(Long collId) {
    // no check for collection existence
    return likeRepository.countAllByCollectionId(collId);
  }

  private Map<Long, Long> getCount(List<Long> collIds) {
    // no check for collection existence
    return likeRepository.countAllGroupByCollectionIdIn(collIds);
  }

  private boolean isLikedCollection(@Nullable Long memberId, Long collId) {
    CollectionLike like = (memberId != null) ?
                          likeRepository.findByMemberIdAndCollectionId(memberId, collId) :
                          null;

    return like != null;
  }

  private Map<Long, Boolean> areLikedCollection(@Nullable Long memberId, List<Long> collIds) {
    Map<Long, Boolean> likedMap = new HashMap<>();
    collIds.forEach(collId -> likedMap.put(collId, false));

    List<CollectionLike> likes = (memberId != null) ?
                                 likeRepository.findAllByMemberIdAndCollectionIdIn(memberId,
                                                                                   collIds) :
                                 Collections.emptyList();

    likes.forEach(like -> likedMap.put(like.getCollection().getId(), true));

    return likedMap;
  }
}
