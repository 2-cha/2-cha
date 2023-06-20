package com._2cha.demo.collection.dto;

import com._2cha.demo.bookmark.dto.BookmarkStatus;
import com._2cha.demo.collection.domain.Collection;
import com._2cha.demo.member.dto.MemberProfileResponse;
import com._2cha.demo.review.dto.LikeStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Data;

@Data
public class CollectionDetailResponse {

  private Long id;
  private String title;
  private String description;
  private String thumbnail;
  private MemberProfileResponse member;
  private List<ReviewInCollectionResponse> reviews;

  @JsonInclude(Include.NON_NULL)
  private BookmarkStatus bookmarkStatus;

  @JsonInclude(Include.NON_NULL)
  private LikeStatus likeStatus;

  public CollectionDetailResponse(Collection collection,
                                  MemberProfileResponse member,
                                  List<ReviewInCollectionResponse> reviews,
                                  String baseUrl) {
    this.id = collection.getId();
    this.title = collection.getTitle();
    this.description = collection.getDescription();
    this.member = member;
    String path = collection.getThumbnailUrlPath();
    this.thumbnail = path != null ? baseUrl + path : null;
    this.reviews = reviews;
  }
}

