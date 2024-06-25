package com.duofuen.permission.domain.repo;

import com.duofuen.permission.domain.entity.DKMember;
import com.duofuen.permission.domain.entity.DKTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface DKTableRepo extends JpaRepository<DKTable, Integer>, JpaSpecificationExecutor<DKTable> {
    Optional<DKTable> findByIdAndDeleted(Integer id , boolean deleted);
    List<DKTable> findAllByDkStoreIdAndDeleted(Integer dkStoreId, boolean deleted);
}
