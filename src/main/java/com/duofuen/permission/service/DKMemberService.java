package com.duofuen.permission.service;

import com.duofuen.permission.domain.entity.DKCatagory;
import com.duofuen.permission.domain.entity.DKMember;
import com.duofuen.permission.domain.repo.DKCatagoryRepo;
import com.duofuen.permission.domain.repo.DKMemberRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DKMemberService {
    private final DKMemberRepo dkMemberRepo;

    public DKMemberService(DKMemberRepo dkMemberRepo) {
        this.dkMemberRepo = dkMemberRepo;
    }

    public Optional<DKMember> findByIdAndDeleted(Integer id, boolean deleted){
        return dkMemberRepo.findByIdAndDeleted(id , false);
    }

    public DKMember save(DKMember catagory){
        return dkMemberRepo.save(catagory);
    }

    public Page<DKMember> findAll(Specification<DKMember> specification , Pageable pageable){
        return dkMemberRepo.findAll(specification , pageable);
    }
    public long count(Specification<DKMember> specification){
        return dkMemberRepo.count(specification);
    }

    public List<DKMember> findAllByDkStoreId(Integer storeId){
        return dkMemberRepo.findAllByDkStoreIdAndDeleted(storeId, false);
    }

    public Optional<DKMember> findByDkStoreIdAndPhoneAndDeleted(Integer dkStoreId, String phone, boolean deleted){
        return dkMemberRepo.findByDkStoreIdAndPhoneAndDeleted(dkStoreId , phone , deleted);
    }
}
