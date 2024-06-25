package com.duofuen.permission.domain.repo;

import com.duofuen.permission.domain.entity.DKCatagory;
import com.duofuen.permission.domain.entity.DKMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface DKMemberRepo extends JpaRepository<DKMember, Integer>, JpaSpecificationExecutor<DKMember> {
    Optional<DKMember> findByIdAndDeleted(Integer id , boolean deleted);
    List<DKMember> findAllByDkStoreIdAndDeleted(Integer dkStoreId, boolean deleted);
    Optional<DKMember> findByDkStoreIdAndPhoneAndDeleted(Integer dkStoreId, String phone, boolean deleted);
}
