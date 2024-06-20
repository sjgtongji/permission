package com.duofuen.permission.service;

import com.duofuen.permission.domain.entity.DKRole;
import com.duofuen.permission.domain.repo.DKRoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DKRoleService {

    private final DKRoleRepo roleRepo;
    @Autowired
    public DKRoleService(DKRoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    public DKRole save(DKRole role){
        return roleRepo.save(role);
    }

    public Page<DKRole> findAll(Pageable pageable){
        return roleRepo.findAll(pageable);
    }

    public Optional<DKRole> findByIdAndDeleted(Integer id, boolean deleted){
        return roleRepo.findByIdAndDeleted(id , false);
    }

    public Page<DKRole> findAll(Specification<DKRole> specification , Pageable pageable){
        return roleRepo.findAll(specification , pageable);
    }

    public long count(Specification<DKRole> specification){
        return roleRepo.count(specification);
    }


    public List<DKRole> saveAll(List<DKRole> roles){
        return roleRepo.saveAll(roles);
    }

}
