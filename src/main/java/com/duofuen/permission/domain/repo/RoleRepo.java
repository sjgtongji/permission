package com.duofuen.permission.domain.repo;

import com.duofuen.permission.domain.entity.Menu;
import com.duofuen.permission.domain.entity.Project;
import com.duofuen.permission.domain.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role , Integer>, JpaSpecificationExecutor<Role> {
    Page<Role> findAll(Specification<Role> specification , Pageable pageable);
    Optional<Role> findByIdAndDeleted(Integer id, boolean deleted);
}
