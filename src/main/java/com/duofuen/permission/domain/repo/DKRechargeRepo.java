package com.duofuen.permission.domain.repo;

import com.duofuen.permission.domain.entity.DKMember;
import com.duofuen.permission.domain.entity.DKRecharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface DKRechargeRepo extends JpaRepository<DKRecharge, Integer>, JpaSpecificationExecutor<DKRecharge> {
    Optional<DKRecharge> findByIdAndDeleted(Integer id , boolean deleted);
    List<DKRecharge> findAllByDkStoreIdAndDeleted(Integer dkStoreId, boolean deleted);
}
