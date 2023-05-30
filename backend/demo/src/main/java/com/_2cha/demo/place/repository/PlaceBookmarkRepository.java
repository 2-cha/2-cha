package com._2cha.demo.place.repository;

import com._2cha.demo.place.domain.PlaceBookmark;
import java.util.List;
import org.springframework.data.repository.Repository;


public interface PlaceBookmarkRepository extends Repository<PlaceBookmark, Long> {

  PlaceBookmark save(PlaceBookmark bookmark);

  PlaceBookmark findByMemberIdAndPlaceId(Long memberId, Long placeId);

  List<PlaceBookmark> findAllByMemberId(Long memberId);

  void delete(PlaceBookmark bookmark);
}
