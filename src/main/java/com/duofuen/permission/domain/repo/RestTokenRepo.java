package com.duofuen.permission.domain.repo;

import com.duofuen.permission.domain.entity.RestToken;
import org.springframework.data.repository.CrudRepository;

public interface RestTokenRepo extends CrudRepository<RestToken, Integer> {
    RestToken findByToken(String token);
    RestToken findByUserId(Integer id);
}
