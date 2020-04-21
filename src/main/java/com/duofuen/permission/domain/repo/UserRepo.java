package com.duofuen.permission.domain.repo;

import com.duofuen.permission.domain.entity.Project;
import com.duofuen.permission.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User , Integer>, JpaSpecificationExecutor<User> {
    Optional<User> findByUserNameAndPassword(String username , String password);
}
