package com._2cha.demo.place.repository;

import com._2cha.demo.place.domain.Place;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface PlaceRepository extends Repository<Place, Long> {

  void save(Place place);

  Place findById(Long id);

  @Query(
      value = "SELECT * FROM PLACE WHERE NAME ~ ?1",
      countQuery = "SELECT COUNT(*) FROM PLACE WHERE NAME ~ ?1",
      nativeQuery = true
  )
  List<Place> findPlacesByNameMatchesRegex(String regex, Pageable pageParam);
}
