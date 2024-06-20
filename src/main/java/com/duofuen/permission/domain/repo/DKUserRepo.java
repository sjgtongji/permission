package com.duofuen.permission.domain.repo;

import com.duofuen.permission.domain.entity.DKUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface DKUserRepo extends JpaRepository<DKUser, Integer>, JpaSpecificationExecutor<DKUser> {
    Optional<DKUser> findByUserNameAndPassword(String username , String password);
    Optional<DKUser> findByIdAndDeleted(Integer id , boolean deleted);
    List<DKUser> findAllByIdInAndDeleted(List<Integer> ids , boolean deleted);

}
