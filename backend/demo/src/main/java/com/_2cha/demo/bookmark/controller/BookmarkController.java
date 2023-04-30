package com._2cha.demo.bookmark.controller;

import com._2cha.demo.bookmark.domain.ItemType;
import com._2cha.demo.bookmark.dto.BookmarkBriefResponse;
import com._2cha.demo.bookmark.dto.BookmarkCreateRequest;
import com._2cha.demo.bookmark.dto.BookmarkCreatedResponse;
import com._2cha.demo.bookmark.dto.BookmarkRemovedResponse;
import com._2cha.demo.bookmark.dto.PlaceBookmarkResponse;
import com._2cha.demo.bookmark.dto.ReviewBookmarkResponse;
import com._2cha.demo.bookmark.service.BookmarkService;
import com._2cha.demo.global.annotation.Auth;
import com._2cha.demo.global.annotation.Authed;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class BookmarkController {

  private final BookmarkService bookmarkService;

  @Auth
  @GetMapping("/bookmarks/reviews")
  public List<BookmarkBriefResponse> getReviewBookmarkList(@Authed Long memberId) {
    return bookmarkService.getReviewBookmarkList(memberId);
  }

  @Auth
  @GetMapping("/bookmarks/collections")
  public List<BookmarkBriefResponse> getCollectionBookmarkList(@Authed Long memberId) {
    return bookmarkService.getCollectionBookmarkList(memberId);
  }

  @Auth
  @GetMapping("/bookmarks/places")
  public List<BookmarkBriefResponse> getPlaceBookmarkList(@Authed Long memberId) {
    return bookmarkService.getPlaceBookmarkList(memberId);
  }

  // scrollable bookmark detail
  @Auth
  @GetMapping("/bookmarks/reviews/{bookmarkId}")
  public ReviewBookmarkResponse getReviewBookmark(@Authed Long memberId,
                                                  @PathVariable Long bookmarkId) {
    return bookmarkService.getReviewBookmark(memberId, bookmarkId);
  }

  //NOTE: collection bookmark directly goes to collection page

  // scrollable bookmark detail
  @Auth
  @GetMapping("/bookmarks/places/{bookmarkId}")
  public PlaceBookmarkResponse getPlaceBookmark(@Authed Long memberId,
                                                @PathVariable Long bookmarkId) {
    return bookmarkService.getPlaceBookmark(memberId, bookmarkId);
  }


  @Auth
  @PostMapping("/bookmarks/{itemType}")
  public BookmarkCreatedResponse createBookmark(@Authed Long memberId,
                                                @PathVariable ItemType itemType,
                                                @RequestBody @Valid BookmarkCreateRequest dto
                                               ) {
    return bookmarkService.createBookmark(memberId, dto.getItemId(), itemType);
  }

  @Auth
  @DeleteMapping("/bookmarks/{bookmarkId}")
  public BookmarkRemovedResponse removeBookmark(@Authed Long memberId,
                                                @PathVariable Long bookmarkId) {
    return bookmarkService.removeBookmark(memberId, bookmarkId);
  }
}
