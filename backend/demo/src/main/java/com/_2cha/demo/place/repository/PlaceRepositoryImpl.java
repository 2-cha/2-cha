package com._2cha.demo.place.repository;

import com._2cha.demo.place.domain.Category;
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

  @Override
  public List<Object[]> findAroundWithTagFilter(Double latitude, Double longitude,
                                                Double minDist, Double maxDist,
                                                Integer pageSize, List<Long> tagIds) {
    /*------------------------
     @ Filter 1: Tag preference
     @ Filter 2: Category
     ------------------------*/

    /*------------------------
     @ Sort 1: Distance
     @ Sort 2: Review count
     @ Sort 3: Specific tag count
     ------------------------*/

    Point location = GeomUtils.createPoint(latitude, longitude);
    //noinspection JpaQlInspection
    return em.createQuery(
                 "select distinct r.place as p, ST_DistanceSphere(r.place.location, :location) as d, r " +
                 "from Review r " +
                 "inner join r.tags t " + // review.getTags() retrieve only joined tag when using join fetch
                 "where t.id in :tagIds " +
                 "and ST_DistanceSphere(r.place.location, :location) > :minDist " +
                 "and ST_DistanceSphere(r.place.location, :location) <= :maxDist " +
                 "order by d", Object[].class)
             .setParameter("tagIds", tagIds)
             .setParameter("location", location)
             .setParameter("minDist", minDist)
             .setParameter("maxDist", maxDist)
             .setMaxResults(pageSize)
             .getResultList();
  }

  @Override
  public List<Object[]> findAroundWithCategoryFilter(Double latitude, Double longitude,
                                                     Double minDist, Double maxDist,
                                                     Integer pageSize, List<Category> categories) {
    Point location = GeomUtils.createPoint(latitude, longitude);
    //noinspection JpaQlInspection
    return em.createQuery(
                 "select p, ST_DistanceSphere(p.location, :location) as d " +
                 "from Place p " +
                 "where p.category in :categories " +
                 "and ST_DistanceSphere(p.location, :location) > :minDist " +
                 "and ST_DistanceSphere(p.location, :location) <= :maxDist " +
                 "order by d", Object[].class)
             .setParameter("location", location)
             .setParameter("categories", categories)
             .setParameter("minDist", minDist)
             .setParameter("maxDist", maxDist)
             .setMaxResults(pageSize)
             .getResultList();
  }
}