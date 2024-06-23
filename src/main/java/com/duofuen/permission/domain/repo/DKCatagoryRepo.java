package com.duofuen.permission.domain.repo;

import com.duofuen.permission.domain.entity.DKCatagory;
import com.duofuen.permission.domain.entity.DKUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface DKCatagoryRepo extends JpaRepository<DKCatagory, Integer>, JpaSpecificationExecutor<DKCatagory> {
    Optional<DKCatagory> findByIdAndDeleted(Integer id , boolean deleted);
    List<DKCatagory> findAllByDkStoreIdAndDeleted(Integer dkStoreId, boolean deleted);
}
