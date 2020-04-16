package com.duofuen.permission.service;

import com.duofuen.permission.domain.entity.Menu;
import com.duofuen.permission.domain.entity.Project;
import com.duofuen.permission.domain.repo.MenuRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MenuService {
    private final MenuRepo menuRepo;

    public MenuService(MenuRepo menuRepo) {
        this.menuRepo = menuRepo;
    }

    public Optional<Menu> findByIdAndDeleted(Integer id, boolean deleted){
        return menuRepo.findById(id);
    }

    public Menu save(Menu menu){
        return menuRepo.save(menu);
    }

    public List<Menu> findAll(){
        List<Menu> menus = new ArrayList<>();
        Iterable<Menu> iterable = menuRepo.findAll();
        for(Menu item : iterable){
            menus.add(item);
        }
        return menus;
    }
    public Page<Menu> findAll(Specification<Menu> specification , Pageable pageable){
        return menuRepo.findAll(specification , pageable);
    }
    public long count(Specification<Menu> specification){
        return menuRepo.count(specification);
    }

    public List<Menu> findAllParent(Pageable pageable){
        Page<Menu> page = menuRepo.findAllByParentIdAndDeleted(null , false , pageable);
        List<Menu> list = new ArrayList<>();
        for(Menu item : page){
            list.add(item);
        }
        return list;
    }

}
