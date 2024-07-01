package com.duofuen.permission.service;

import com.duofuen.permission.domain.entity.DKTableOrder;
import com.duofuen.permission.domain.entity.DKTableOrderGoods;
import com.duofuen.permission.domain.repo.DKTableOrderGoodsRepo;
import com.duofuen.permission.domain.repo.DKTableOrderRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class DKTableOrderGoodsService {
    private final DKTableOrderGoodsRepo dkTableOrderGoodsRepo;

    public DKTableOrderGoodsService(DKTableOrderGoodsRepo dkTableOrderGoodsRepo) {
        this.dkTableOrderGoodsRepo = dkTableOrderGoodsRepo;
    }

    public Optional<DKTableOrderGoods> findByIdAndDeleted(Integer id, boolean deleted){
        return dkTableOrderGoodsRepo.findByIdAndDeleted(id , false);
    }

    public DKTableOrderGoods save(DKTableOrderGoods catagory){
        return dkTableOrderGoodsRepo.save(catagory);
    }

    public Page<DKTableOrderGoods> findAll(Specification<DKTableOrderGoods> specification , Pageable pageable){
        return dkTableOrderGoodsRepo.findAll(specification , pageable);
    }
    public long count(Specification<DKTableOrderGoods> specification){
        return dkTableOrderGoodsRepo.count(specification);
    }

    public List<DKTableOrderGoods> findAllByDkStoreId(Integer storeId){
        return dkTableOrderGoodsRepo.findAllByDkStoreIdAndDeleted(storeId, false);
    }
    public List<DKTableOrderGoods> findAllByDkStoreIdAndDkTableOrderId(Integer dkStoreId, Integer dkTableOrderId){
        return dkTableOrderGoodsRepo.findAllByDkStoreIdAndDkTableOrderIdAndDeleted(dkStoreId , dkTableOrderId ,false);
    }
}
