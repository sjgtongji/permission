package com.duofuen.permission.service;

import com.duofuen.permission.domain.entity.DKMenu;
import com.duofuen.permission.domain.repo.DKMenuRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DKMenuService {
    private final DKMenuRepo menuRepo;

    public DKMenuService(DKMenuRepo menuRepo) {
        this.menuRepo = menuRepo;
    }

    public Optional<DKMenu> findByIdAndDeleted(Integer id, boolean deleted){
        return menuRepo.findByIdAndDeleted(id , false);
    }

    public DKMenu save(DKMenu menu){
        return menuRepo.save(menu);
    }
}
