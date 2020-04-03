package com.duofuen.permission.domain.repo;

import com.duofuen.permission.domain.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ProjectRepo extends JpaRepository<Project , Integer> , JpaSpecificationExecutor<Project> {
    Optional<Project> findByAppIdAndAppSecret(String appId, String appSecret);
}
