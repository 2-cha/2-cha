package com._2cha.demo.place.repository;

import com._2cha.demo.place.domain.Category;
import com._2cha.demo.place.domain.Place;
import java.util.List;

public interface PlaceRepository {

  public Place findById(Long id);


  public Place findByName(String name);

  public List<Place> findAll();


  public void save(Place place);

  //TODO: spatial index

  /**
   * @param latitude
   * @param longitude
   * @param minDist
   * @param maxDist
   * @param pageSize
   * @return { Place, Double(distance gap) }
   */
  List<Object[]> findAround(Double latitude, Double longitude,
                            Double minDist, Double maxDist, Integer pageSize);

  List<Object[]> findAroundWithTagFilter(Double latitude, Double longitude,
                                         Double minDist, Double maxDist,
                                         Integer pageSize, List<Long> tagIds);

  List<Object[]> findAroundWithCategoryFilter(Double latitude, Double longitude,
                                              Double minDist, Double maxDist,
                                              Integer pageSize, List<Category> categories);
}
