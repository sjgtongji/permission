package com.duofuen.permission.domain.repo;

import com.duofuen.permission.domain.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MenuRepo extends PagingAndSortingRepository<Menu , Integer> {
    Page<Menu> findAllByParentId(Integer parentId , Pageable pageable);
}
