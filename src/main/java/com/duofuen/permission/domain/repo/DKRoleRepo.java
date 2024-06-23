package com.duofuen.permission.domain.repo;

import com.duofuen.permission.domain.entity.DKRole;
import com.duofuen.permission.domain.entity.DKStore;
import com.duofuen.permission.domain.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface DKRoleRepo extends JpaRepository<DKRole, Integer>, JpaSpecificationExecutor<DKRole> {

    Page<DKRole> findAll(Specification<DKRole> specification , Pageable pageable);
    Optional<DKRole> findByIdAndDeleted(Integer id, boolean deleted);
    List<DKRole> findAllByDeleted(boolean deleted);
    List<DKRole> findAllByDkStoreIdAndDeleted(Integer dkStoreId, boolean deleted);
}
