package com.duofuen.permission.service;

import com.duofuen.permission.domain.entity.DKMember;
import com.duofuen.permission.domain.entity.DKTable;
import com.duofuen.permission.domain.repo.DKMemberRepo;
import com.duofuen.permission.domain.repo.DKTableRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DKTableService {
    private final DKTableRepo dkTableRepo;

    public DKTableService(DKTableRepo dkTableRepo) {
        this.dkTableRepo = dkTableRepo;
    }

    public Optional<DKTable> findByIdAndDeleted(Integer id, boolean deleted){
        return dkTableRepo.findByIdAndDeleted(id , false);
    }

    public DKTable save(DKTable catagory){
        return dkTableRepo.save(catagory);
    }

    public Page<DKTable> findAll(Specification<DKTable> specification , Pageable pageable){
        return dkTableRepo.findAll(specification , pageable);
    }
    public long count(Specification<DKTable> specification){
        return dkTableRepo.count(specification);
    }

    public List<DKTable> findAllByDkStoreId(Integer storeId){
        return dkTableRepo.findAllByDkStoreIdAndDeleted(storeId, false);
    }
}
