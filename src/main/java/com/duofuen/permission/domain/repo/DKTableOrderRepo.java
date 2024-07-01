package com.duofuen.permission.domain.repo;

import com.duofuen.permission.domain.entity.DKStore;
import com.duofuen.permission.domain.entity.DKTable;
import com.duofuen.permission.domain.entity.DKTableOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface DKTableOrderRepo extends JpaRepository<DKTableOrder, Integer>, JpaSpecificationExecutor<DKTableOrder> {
    Optional<DKTableOrder> findByIdAndDeleted(Integer id , boolean deleted);
    List<DKTableOrder> findAllByDkStoreIdAndDeleted(Integer dkStoreId, boolean deleted);
    Optional<DKTableOrder> findByDkStoreIdAndDkTableIdAndOpenedAndDeleted(Integer dkStoreId, Integer dkTableId , boolean opened, boolean deleted);
}
