package com.duofuen.permission.service;

import com.duofuen.permission.domain.entity.DKCatagory;
import com.duofuen.permission.domain.entity.DKMenu;
import com.duofuen.permission.domain.repo.DKCatagoryRepo;
import com.duofuen.permission.domain.repo.DKMenuRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class DKCatagoryService {
    private final DKCatagoryRepo catagoryRepo;

    public DKCatagoryService(DKCatagoryRepo catagoryRepo) {
        this.catagoryRepo = catagoryRepo;
    }

    public Optional<DKCatagory> findByIdAndDeleted(Integer id, boolean deleted){
        return catagoryRepo.findByIdAndDeleted(id , false);
    }

    public DKCatagory save(DKCatagory catagory){
        return catagoryRepo.save(catagory);
    }

    public Page<DKCatagory> findAll(Specification<DKCatagory> specification , Pageable pageable){
        return catagoryRepo.findAll(specification , pageable);
    }
    public long count(Specification<DKCatagory> specification){
        return catagoryRepo.count(specification);
    }

    public List<DKCatagory> findAllByDkStoreId(Integer storeId){
        return catagoryRepo.findAllByDkStoreIdAndDeleted(storeId, false);
    }
}
