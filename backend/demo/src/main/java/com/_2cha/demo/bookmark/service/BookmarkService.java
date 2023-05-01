package com._2cha.demo.bookmark.service;

import com._2cha.demo.bookmark.domain.Bookmark;
import com._2cha.demo.bookmark.domain.ItemType;
import com._2cha.demo.bookmark.domain.PlaceBookmark;
import com._2cha.demo.bookmark.domain.ReviewBookmark;
import com._2cha.demo.bookmark.dto.BookmarkBriefResponse;
import com._2cha.demo.bookmark.dto.BookmarkCreatedResponse;
import com._2cha.demo.bookmark.dto.BookmarkRemovedResponse;
import com._2cha.demo.bookmark.dto.PlaceBookmarkResponse;
import com._2cha.demo.bookmark.dto.ReviewBookmarkResponse;
import com._2cha.demo.bookmark.repository.BookmarkQueryRepository;
import com._2cha.demo.bookmark.repository.BookmarkRepository;
import com._2cha.demo.bookmark.service.itemfinder.BookmarkItemFinder;
import com._2cha.demo.bookmark.service.itemfinder.BookmarkItemFinderFactory;
import com._2cha.demo.global.exception.BadRequestException;
import com._2cha.demo.global.exception.ConflictException;
import com._2cha.demo.global.exception.ForbiddenException;
import com._2cha.demo.global.exception.NotFoundException;
import com._2cha.demo.member.domain.Member;
import com._2cha.demo.member.dto.MemberProfileResponse;
import com._2cha.demo.member.service.MemberService;
import com._2cha.demo.place.dto.PlaceBriefResponse;
import com._2cha.demo.place.service.PlaceService;
import com._2cha.demo.review.domain.Review;
import com._2cha.demo.review.dto.ReviewResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BookmarkService {

  private final BookmarkRepository bookmarkRepository;
  private final BookmarkQueryRepository bookmarkQueryRepository;
  private final BookmarkItemFinderFactory itemFinderFactory;
  private final MemberService memberService;
  private final PlaceService placeService;

  /*-----------
   @ Queries
   ----------*/
  public List<BookmarkBriefResponse> getReviewBookmarkList(Long memberId) {
    return bookmarkQueryRepository.getReviewBookmarksByMemberId(memberId);
  }

  public List<BookmarkBriefResponse> getCollectionBookmarkList(Long memberId) {
    return bookmarkQueryRepository.getCollectionBookmarksByMemberId(memberId);
  }

  public List<BookmarkBriefResponse> getPlaceBookmarkList(Long memberId) {
    return bookmarkQueryRepository.getPlaceBookmarksByMemberId(memberId);
  }

  // scrollable bookmark detail
  public ReviewBookmarkResponse getReviewBookmark(Long memberId, Long id) {
    final int PLACE_TAG_SUMMARY_SIZE = 3;

    Bookmark bookmark = bookmarkRepository.findById(id);
    if (bookmark == null) {
      throw new NotFoundException("No bookmark with such id.", "noSuchBookmark");
    }
    if (bookmark.getMember().getId() != memberId) {
      throw new ForbiddenException("Cannot get other member's bookmark");
    }
    if (!(bookmark instanceof ReviewBookmark reviewBookmark)) {
      throw new BadRequestException("Bookmark type mismatched", "wrongBookmarkType");
    }

    Review review = reviewBookmark.getItem();
    MemberProfileResponse reviewWriter = memberService.getMemberProfileById(
        review.getMember().getId());
    PlaceBriefResponse place = placeService.getPlaceBriefById(review.getPlace().getId(),
                                                              PLACE_TAG_SUMMARY_SIZE);

    return new ReviewBookmarkResponse(reviewBookmark.getId(),
                                      new ReviewResponse(
                                          reviewBookmark.getItem(),
                                          reviewWriter,
                                          place));
  }

  // scrollable bookmark detail
  public PlaceBookmarkResponse getPlaceBookmark(Long memberId, Long id) {
    Bookmark bookmark = bookmarkRepository.findById(id);
    if (bookmark == null) {
      throw new NotFoundException("No bookmark with such id.", "noSuchBookmark");
    }
    if (bookmark.getMember().getId() != memberId) {
      throw new ForbiddenException("Cannot remove other member's bookmark");
    }
    if (!(bookmark instanceof PlaceBookmark placeBookmark)) {
      throw new BadRequestException("Bookmark type mismatched", "wrongBookmarkType");
    }

    return new PlaceBookmarkResponse(placeBookmark.getId(),
                                     new PlaceBriefResponse(placeBookmark.getItem()));
  }


  /*-----------
   @ Commands
   ----------*/
  @Transactional
  public BookmarkCreatedResponse createBookmark(Long memberId, Long itemId, ItemType itemType) {
    Member member = memberService.findById(memberId);
    BookmarkItemFinder itemFinder = itemFinderFactory.getItemFinder(itemType);
    Object item = itemFinder.findItemById(itemId);
    if (item == null) throw new NotFoundException("No item with such id.", "noSuchItem");
    
    if (bookmarkRepository.findMemberBookmarkItem(itemType, itemId, memberId) != null) {
      throw new ConflictException("Already bookmarked item.", "alreadyBookmarked");
    }
    Bookmark bookmark = Bookmark.createBookmark(member, item);

    bookmarkRepository.save(bookmark);

    return new BookmarkCreatedResponse(bookmark);
  }

  @Transactional
  public BookmarkRemovedResponse removeBookmark(Long memberId, Long bookmarkId) {
    Bookmark bookmark = bookmarkRepository.findById(bookmarkId);
    if (bookmark == null) {
      throw new NotFoundException("No bookmark with such id.", "noSuchBookmark");
    }
    if (bookmark.getMember().getId() != memberId) {
      throw new ForbiddenException("Cannot remove other member's bookmark");
    }

    bookmarkRepository.deleteBookmarkById(bookmark.getId());

    return new BookmarkRemovedResponse(bookmark);
  }
}
