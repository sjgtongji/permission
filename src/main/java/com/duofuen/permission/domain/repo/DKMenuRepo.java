package com.duofuen.permission.domain.repo;

import com.duofuen.permission.domain.entity.DKMenu;
import com.duofuen.permission.domain.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface DKMenuRepo extends JpaRepository<DKMenu , Integer>, JpaSpecificationExecutor<DKMenu> {

    Page<DKMenu> findAllByStoreIdAndDeleted(Integer storeId , boolean deleted , Pageable pageable);
    Page<DKMenu> findAll(Specification<DKMenu> specification , Pageable pageable);
    Optional<DKMenu> findByIdAndDeleted(Integer id, boolean deleted);
}
