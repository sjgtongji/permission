package com.duofuen.permission.domain.repo;

import com.duofuen.permission.domain.entity.Menu;
import com.duofuen.permission.domain.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepo extends JpaRepository<Menu , Integer>, JpaSpecificationExecutor<Menu> {
    Page<Menu> findAllByParentId(Integer parentId , Pageable pageable);
    Page<Menu> findAllByParentIdAndDeleted(Integer parentId , boolean deleted , Pageable pageable);
    Page<Menu> findAll(Specification<Menu> specification , Pageable pageable);
    Optional<Menu> findByIdAndDeleted(Integer id, boolean deleted);
    List<Menu> findAllByParentIdAndDeleted(Integer parentId , boolean deleted);
}
