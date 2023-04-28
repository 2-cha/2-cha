package com._2cha.demo.bookmark.service;

import com._2cha.demo.bookmark.domain.Bookmark;
import com._2cha.demo.bookmark.domain.ItemType;
import com._2cha.demo.bookmark.domain.PlaceBookmark;
import com._2cha.demo.bookmark.domain.ReviewBookmark;
import com._2cha.demo.bookmark.dto.BookmarkBriefResponse;
import com._2cha.demo.bookmark.dto.PlaceBookmarkResponse;
import com._2cha.demo.bookmark.repository.BookmarkQueryRepository;
import com._2cha.demo.bookmark.repository.BookmarkRepository;
import com._2cha.demo.bookmark.service.itemfinder.BookmarkItemFinder;
import com._2cha.demo.bookmark.service.itemfinder.BookmarkItemFinderFactory;
import com._2cha.demo.global.exception.BadRequestException;
import com._2cha.demo.member.domain.Member;
import com._2cha.demo.member.service.MemberService;
import com._2cha.demo.review.dto.PlaceReviewResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookmarkService {

  private final BookmarkRepository bookmarkRepository;
  private final BookmarkQueryRepository bookmarkQueryRepository;
  private final BookmarkItemFinderFactory itemFinderFactory;
  private final MemberService memberService;

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
  public void getReviewBookmark(Long id) {
    Bookmark bookmark = bookmarkRepository.findById(id);
    if (!(bookmark instanceof ReviewBookmark)) {
      throw new BadRequestException("Bookmark type mismatched", "wrongBookmarkType");
    }

    ReviewBookmark reviewBookmark = (ReviewBookmark) bookmark;

    //TODO: refactor as full ReviewResponse (with member, place);
    new PlaceReviewResponse(reviewBookmark.getItem());
  }

  // scrollable bookmark detail
  public PlaceBookmarkResponse getPlaceBookmark(Long id) {
    Bookmark bookmark = bookmarkRepository.findById(id);
    if (!(bookmark instanceof PlaceBookmark)) {
      throw new BadRequestException("Bookmark type mismatched", "wrongBookmarkType");
    }

    return new PlaceBookmarkResponse((PlaceBookmark) bookmark);
  }


  /*-----------
   @ Commands
   ----------*/
  public void createBookmark(Long memberId, Long itemId, ItemType itemType) {
    Member member = memberService.findById(memberId);
    BookmarkItemFinder itemFinder = itemFinderFactory.getItemFinder(itemType);
    Object item = itemFinder.findItemById(itemId);

    Bookmark bookmark = Bookmark.createBookmark(member, item);

    bookmarkRepository.save(bookmark);
  }

  public void removeBookmark(Long memberId, Long bookmarkId) {
    Bookmark bookmark = bookmarkRepository.findById(bookmarkId);
    if (bookmark == null) ;
    if (bookmark.getMember().getId() != memberId) ;

    bookmarkRepository.deleteBookmarkById(bookmark.getId());
  }
}
