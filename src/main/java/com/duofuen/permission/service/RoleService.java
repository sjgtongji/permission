package com.duofuen.permission.service;

import com.duofuen.permission.domain.entity.Project;
import com.duofuen.permission.domain.entity.Role;
import com.duofuen.permission.domain.repo.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepo roleRepo;
    @Autowired
    public RoleService(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    public Role save(Role role){
        return roleRepo.save(role);
    }

    public Page<Role> findAll(Pageable pageable){
        return roleRepo.findAll(pageable);
    }

    public Optional<Role> findByIdAndDeleted(Integer id, boolean deleted){
        return roleRepo.findByIdAndDeleted(id , false);
    }

    public Page<Role> findAll(Specification<Role> specification , Pageable pageable){
        return roleRepo.findAll(specification , pageable);
    }

    public long count(Specification<Role> specification){
        return roleRepo.count(specification);
    }

    public List<Role> findAllByCodeAndDeleted(String code , boolean deleted){
        return roleRepo.findAllByCodeAndDeleted(code , deleted);
    }

    public List<Role> findAllByIdInAndDeleted(int[] ids , boolean deleted){
        List<Integer> list = new ArrayList<>();
        for(int i : ids){
            list.add(i);
        }
        return roleRepo.findAllByIdInAndDeleted(list , deleted);
    }
    public List<Role> saveAll(List<Role> roles){
        return roleRepo.saveAll(roles);
    }
}
