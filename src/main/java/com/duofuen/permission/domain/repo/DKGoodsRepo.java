package com.duofuen.permission.domain.repo;

import com.duofuen.permission.domain.entity.DKCatagory;
import com.duofuen.permission.domain.entity.DKGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface DKGoodsRepo extends JpaRepository<DKGoods, Integer>, JpaSpecificationExecutor<DKGoods> {
    Optional<DKGoods> findByIdAndDeleted(Integer id , boolean deleted);
    List<DKGoods> findAllByDkCatagoryIdAndDeleted(Integer dkCatagoryId, boolean deleted);
    List<DKGoods> findAllByDkStoreIdAndDkCatagoryIdAndDeleted(Integer dkStoreId , Integer dkCatagoryId, boolean deleted);
}
