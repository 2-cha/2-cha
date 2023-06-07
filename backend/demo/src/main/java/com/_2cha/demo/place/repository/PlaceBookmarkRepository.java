package com._2cha.demo.place.repository;

import com._2cha.demo.bookmark.dto.BookmarkCountProjection;
import com._2cha.demo.place.domain.PlaceBookmark;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;


public interface PlaceBookmarkRepository extends Repository<PlaceBookmark, Long> {

  PlaceBookmark save(PlaceBookmark bookmark);

  PlaceBookmark findByMemberIdAndPlaceId(Long memberId, Long placeId);

  List<PlaceBookmark> findAllByMemberIdAndPlaceIdIn(Long memberId, List<Long> placeIds);

  List<PlaceBookmark> findAllByMemberId(Long memberId);

  @Query("SELECT b.place.id as id, count(b) as count " +
         "FROM PlaceBookmark b " +
         "WHERE b.place.id in ?1 GROUP BY id")
  List<BookmarkCountProjection> countAllByPlaceIdIn(List<Long> placeId);

  Long countAllByPlaceId(Long placeId);

  void delete(PlaceBookmark bookmark);
}
