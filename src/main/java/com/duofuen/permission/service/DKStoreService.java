package com.duofuen.permission.service;

import com.duofuen.permission.domain.entity.DKStore;
import com.duofuen.permission.domain.entity.DKUser;
import com.duofuen.permission.domain.repo.DKStoreRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DKStoreService {
    private final DKStoreRepo dkStoreRepo;

    public DKStoreService(DKStoreRepo dkStoreRepo) {
        this.dkStoreRepo = dkStoreRepo;
    }

    public DKStore save(DKStore store){
        return dkStoreRepo.save(store);
    }

    public Optional<DKStore> findByIdAndDeleted(Integer id, boolean deleted){
        return dkStoreRepo.findByIdAndDeleted(id , false);
    }

    public Page<DKStore> findAll(Specification<DKStore> specification, Pageable pageable) {
        return dkStoreRepo.findAll(specification , pageable);
    }

    public long count(Specification<DKStore> specification){
        return dkStoreRepo.count(specification);
    }

    public List<DKStore> findAllForSelect(){
        return dkStoreRepo.findAllByDeleted(false);
    }
}
