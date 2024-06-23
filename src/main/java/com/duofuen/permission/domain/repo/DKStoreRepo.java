package com.duofuen.permission.domain.repo;

import com.duofuen.permission.domain.entity.DKStore;
import com.duofuen.permission.domain.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;


public interface DKStoreRepo extends JpaRepository<DKStore, Integer>, JpaSpecificationExecutor<DKStore> {
    Optional<DKStore> findByIdAndDeleted(Integer id , boolean deleted);
    List<DKStore> findAllByIdAndDeleted(List<Integer> ids , boolean deleted);
    List<DKStore> findAllByDeleted(boolean deleted);
}

