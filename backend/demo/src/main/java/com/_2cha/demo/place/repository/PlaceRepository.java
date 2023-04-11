package com._2cha.demo.place.repository;

import com._2cha.demo.place.domain.Place;
import com._2cha.demo.place.dto.FilterBy;
import com._2cha.demo.place.dto.SortBy;
import java.util.List;

public interface PlaceRepository {

  Place findById(Long id);

  List<Place> findByIdIn(List<Long> ids);


  Place findByName(String name);

  List<Place> findAll();

  void save(Place place);

  List<Object[]> findAround(Double latitude, Double longitude,
                            Double minDist, Double maxDist,
                            Integer pageSize,
                            SortBy sortBy, FilterBy filterBy,
                            List<?> filterValues);
}
