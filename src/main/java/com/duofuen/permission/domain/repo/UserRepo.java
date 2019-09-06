package com.duofuen.permission.domain.repo;

import com.duofuen.permission.domain.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepo extends PagingAndSortingRepository<User , Integer> {
}
