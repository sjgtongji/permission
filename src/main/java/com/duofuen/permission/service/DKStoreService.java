package com.duofuen.permission.service;

import com.duofuen.permission.domain.entity.DKStore;
import com.duofuen.permission.domain.repo.DKStoreRepo;
import org.springframework.stereotype.Service;

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
}
