package com.duofuen.permission.service;

import com.duofuen.permission.domain.entity.DKMenu;
import com.duofuen.permission.domain.entity.Menu;
import com.duofuen.permission.domain.repo.DKMenuRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public Page<DKMenu> findAll(Specification<DKMenu> specification , Pageable pageable){
        return menuRepo.findAll(specification , pageable);
    }
    public long count(Specification<DKMenu> specification){
        return menuRepo.count(specification);
    }

    public List<DKMenu> findAllByDkStoreId(Integer storeId){
        return menuRepo.findAllByDkStoreIdAndDeleted(storeId, false);
    }

}
