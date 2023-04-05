package com._2cha.demo.place.repository;

import com._2cha.demo.place.domain.Place;
import com._2cha.demo.util.GeomUtils;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class PlaceRepositoryImpl implements PlaceRepository {

  private final EntityManager em;

  @Override
  public void save(Place place) {
    em.persist(place);
  }

  @Override
  public Place findById(Long id) {
    return em.find(Place.class, id);
  }

  @Override
  public Place findByName(String name) {
    return em.createQuery("select p from Place p where name like :name", Place.class)
             .setParameter("name", name)
             .getSingleResult();
  }

  @Override
  public List<Place> findAll() {
    return em.createQuery("select p from Place p", Place.class)
             .getResultList();
  }


  @Override
  public List<Object[]> findAround(Double latitude, Double longitude,
                                   Double minDist, Double maxDist,
                                   Integer pageSize) {
    Point location = GeomUtils.createPoint(latitude, longitude);
    //noinspection JpaQlInspection
    return em.createQuery(
                 "select p, ST_DistanceSphere(p.location, :location) as d " +
                 "from Place p " +
                 "where ST_DistanceSphere(p.location, :location) > :minDist " +
                 "and ST_DistanceSphere(p.location, :location) <= :maxDist " +
                 "order by d", Object[].class)
             .setParameter("location", location)
             .setParameter("minDist", minDist)
             .setParameter("maxDist", maxDist)
             .setMaxResults(pageSize)
             .getResultList();
  }
}