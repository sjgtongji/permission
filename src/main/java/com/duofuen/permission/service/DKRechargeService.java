package com.duofuen.permission.service;

import com.duofuen.permission.domain.entity.DKMember;
import com.duofuen.permission.domain.entity.DKRecharge;
import com.duofuen.permission.domain.repo.DKMemberRepo;
import com.duofuen.permission.domain.repo.DKRechargeRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DKRechargeService {
    private final DKRechargeRepo dkRechargeRepo;

    public DKRechargeService(DKRechargeRepo dkRechargeRepo) {
        this.dkRechargeRepo = dkRechargeRepo;
    }

    public Optional<DKRecharge> findByIdAndDeleted(Integer id, boolean deleted){
        return dkRechargeRepo.findByIdAndDeleted(id , false);
    }

    public DKRecharge save(DKRecharge catagory){
        return dkRechargeRepo.save(catagory);
    }

    public Page<DKRecharge> findAll(Specification<DKRecharge> specification , Pageable pageable){
        return dkRechargeRepo.findAll(specification , pageable);
    }
    public long count(Specification<DKRecharge> specification){
        return dkRechargeRepo.count(specification);
    }

    public List<DKRecharge> findAllByDkStoreId(Integer storeId){
        return dkRechargeRepo.findAllByDkStoreIdAndDeleted(storeId, false);
    }
}
