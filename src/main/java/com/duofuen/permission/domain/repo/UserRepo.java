package com.duofuen.permission.domain.repo;

import com.duofuen.permission.domain.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User , Integer> {
}
