package com.duofuen.permission.domain.repo;

import com.duofuen.permission.domain.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ProjectRepo extends PagingAndSortingRepository<Project , Integer> {
    Optional<Project> findByAppIdAndAppSecret(String appId, String appSecret);
    Page<Project> findAll(Pageable pageable);
}
