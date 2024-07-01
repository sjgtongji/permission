package com.duofuen.permission.service;

import com.duofuen.permission.domain.entity.DKRecharge;
import com.duofuen.permission.domain.entity.DKTableOrder;
import com.duofuen.permission.domain.repo.DKRechargeRepo;
import com.duofuen.permission.domain.repo.DKTableOrderRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class DKTableOrderService {
    private final DKTableOrderRepo dkTableOrderRepo;

    public DKTableOrderService(DKTableOrderRepo dkTableOrderRepo) {
        this.dkTableOrderRepo = dkTableOrderRepo;
    }

    public Optional<DKTableOrder> findByIdAndDeleted(Integer id, boolean deleted){
        return dkTableOrderRepo.findByIdAndDeleted(id , false);
    }

    public DKTableOrder save(DKTableOrder catagory){
        return dkTableOrderRepo.save(catagory);
    }

    public Page<DKTableOrder> findAll(Specification<DKTableOrder> specification , Pageable pageable){
        return dkTableOrderRepo.findAll(specification , pageable);
    }
    public long count(Specification<DKTableOrder> specification){
        return dkTableOrderRepo.count(specification);
    }

    public List<DKTableOrder> findAllByDkStoreId(Integer storeId){
        return dkTableOrderRepo.findAllByDkStoreIdAndDeleted(storeId, false);
    }
    public Optional<DKTableOrder> findByDkStoreIdAndDkTableIdAndOpened(Integer dkStoreId, Integer dkTableId, boolean opened){
        return dkTableOrderRepo.findByDkStoreIdAndDkTableIdAndOpenedAndDeleted(dkStoreId , dkTableId , opened,false);
    }
}
