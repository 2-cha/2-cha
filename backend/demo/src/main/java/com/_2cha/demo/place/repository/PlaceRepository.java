package com._2cha.demo.place.repository;

import com._2cha.demo.place.domain.Place;
import org.springframework.data.repository.Repository;

public interface PlaceRepository extends Repository<Place, Long> {

  void save(Place place);

  Place findById(Long id);
}
