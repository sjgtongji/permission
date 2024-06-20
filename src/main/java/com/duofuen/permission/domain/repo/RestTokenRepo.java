package com.duofuen.permission.domain.repo;

import com.duofuen.permission.domain.entity.RestToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RestTokenRepo extends CrudRepository<RestToken, Integer> {
    Optional<RestToken> findByToken(String token);
    Optional<RestToken> findByUserId(Integer id);
}
