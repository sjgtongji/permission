package com.duofuen.permission.domain.repo;

import com.duofuen.permission.domain.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepo extends PagingAndSortingRepository<User , Integer> {
    Optional<User> findByUserNameAndPassword(String username , String password);
}
