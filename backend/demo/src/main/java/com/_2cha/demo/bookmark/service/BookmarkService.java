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
import com._2cha.demo.bookmark.exception.BookmarkTypeMismatchedException;
import com._2cha.demo.bookmark.exception.CannotGetException;
import com._2cha.demo.bookmark.exception.CannotRemoveException;
import com._2cha.demo.bookmark.exception.DuplicatedBookmarkItemException;
import com._2cha.demo.bookmark.exception.NoSuchBookmarkException;
import com._2cha.demo.bookmark.exception.NoSuchItemException;
import com._2cha.demo.bookmark.repository.BookmarkQueryRepository;
import com._2cha.demo.bookmark.repository.BookmarkRepository;
import com._2cha.demo.bookmark.service.itemfinder.BookmarkItemFinder;
import com._2cha.demo.bookmark.service.itemfinder.BookmarkItemFinderFactory;
import com._2cha.demo.global.infra.storage.service.FileStorageService;
import com._2cha.demo.member.domain.Member;
import com._2cha.demo.member.dto.MemberProfileResponse;
import com._2cha.demo.member.service.MemberService;
import com._2cha.demo.place.dto.PlaceBriefResponse;
import com._2cha.demo.place.service.PlaceService;
import com._2cha.demo.review.domain.Review;
import com._2cha.demo.review.dto.ReviewResponse;
import java.util.List;
import java.util.Objects;
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
  private final FileStorageService fileStorageService;
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
      throw new NoSuchBookmarkException();
    }
    if (!Objects.equals(bookmark.getMember().getId(), memberId)) {
      throw new CannotGetException();
    }
    if (!(bookmark instanceof ReviewBookmark reviewBookmark)) {
      throw new BookmarkTypeMismatchedException();
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
      throw new NoSuchBookmarkException();
    }
    if (!Objects.equals(bookmark.getMember().getId(), memberId)) {
      throw new CannotRemoveException();
    }
    if (!(bookmark instanceof PlaceBookmark placeBookmark)) {
      throw new BookmarkTypeMismatchedException();
    }

    return new PlaceBookmarkResponse(placeBookmark.getId(),
                                     new PlaceBriefResponse(placeBookmark.getItem(),
                                                            fileStorageService.getBaseUrl()));
  }


  /*-----------
   @ Commands
   ----------*/
  @Transactional
  public BookmarkCreatedResponse createBookmark(Long memberId, Long itemId, ItemType itemType) {
    Member member = memberService.findById(memberId);
    BookmarkItemFinder itemFinder = itemFinderFactory.getItemFinder(itemType);
    Object item = itemFinder.findItemById(itemId);
    if (item == null) throw new NoSuchItemException();

    if (bookmarkRepository.findMemberBookmarkItem(itemType, itemId, memberId) != null) {
      throw new DuplicatedBookmarkItemException();
    }
    Bookmark bookmark = Bookmark.createBookmark(member, item);

    bookmarkRepository.save(bookmark);

    return new BookmarkCreatedResponse(bookmark);
  }

  @Transactional
  public BookmarkRemovedResponse removeBookmark(Long memberId, Long bookmarkId) {
    Bookmark bookmark = bookmarkRepository.findById(bookmarkId);
    if (bookmark == null) {
      throw new NoSuchBookmarkException();
    }
    if (!Objects.equals(bookmark.getMember().getId(), memberId)) {
      throw new CannotRemoveException();
    }

    bookmarkRepository.deleteBookmarkById(bookmark.getId());

    return new BookmarkRemovedResponse(bookmark);
  }
}
