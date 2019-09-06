package com.duofuen.permission.domain.repo;

import com.duofuen.permission.domain.entity.Project;
import com.duofuen.permission.domain.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RoleRepo extends PagingAndSortingRepository<Role , Integer> {
    Page<Role> findAll(Pageable pageable);
}
