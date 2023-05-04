package com._2cha.demo.auth.repository;

import org.springframework.data.repository.Repository;

public interface TokenRepository extends Repository<RefreshToken, Long> {

  void save(RefreshToken token);

  RefreshToken findById(Long id);
}
