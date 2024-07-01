package com.duofuen.permission.service;

import com.duofuen.permission.domain.entity.DKCatagory;
import com.duofuen.permission.domain.entity.DKGoods;
import com.duofuen.permission.domain.repo.DKCatagoryRepo;
import com.duofuen.permission.domain.repo.DKGoodsRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class DKGoodsService {
    private final DKGoodsRepo goodsRepo;

    public DKGoodsService(DKGoodsRepo goodsRepo) {
        this.goodsRepo = goodsRepo;
    }

    public Optional<DKGoods> findByIdAndDeleted(Integer id, boolean deleted){
        return goodsRepo.findByIdAndDeleted(id , false);
    }

    public DKGoods save(DKGoods catagory){
        return goodsRepo.save(catagory);
    }

    public Page<DKGoods> findAll(Specification<DKGoods> specification , Pageable pageable){
        return goodsRepo.findAll(specification , pageable);
    }
    public long count(Specification<DKGoods> specification){
        return goodsRepo.count(specification);
    }

    public List<DKGoods> findAllByDkCatagoryId(Integer catagoryId){
        return goodsRepo.findAllByDkCatagoryIdAndDeleted(catagoryId, false);
    }
    public List<DKGoods>  findAllByDkStoreIdAndDkCatagoryIdAndDeleted(Integer dkStoreId, Integer catagoryId, boolean deleted){
        return goodsRepo.findAllByDkStoreIdAndDkCatagoryIdAndDeleted(dkStoreId , catagoryId, false);
    }
}
