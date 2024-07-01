package com.duofuen.permission.domain.repo;

import com.duofuen.permission.domain.entity.DKTableOrder;
import com.duofuen.permission.domain.entity.DKTableOrderGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface DKTableOrderGoodsRepo extends JpaRepository<DKTableOrderGoods, Integer>, JpaSpecificationExecutor<DKTableOrderGoods> {
    Optional<DKTableOrderGoods> findByIdAndDeleted(Integer id , boolean deleted);
    List<DKTableOrderGoods> findAllByDkStoreIdAndDeleted(Integer dkStoreId, boolean deleted);
    List<DKTableOrderGoods> findAllByDkStoreIdAndDkTableOrderIdAndDeleted(Integer dkStoreId, Integer dkTableOrderId, boolean deleted);
}
