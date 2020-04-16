package com.duofuen.permission.service;

import com.duofuen.permission.domain.entity.Project;
import com.duofuen.permission.domain.repo.ProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectRepo projectRepo;

    @Autowired
    public ProjectService(ProjectRepo projectRepo) {
        this.projectRepo = projectRepo;
    }

    public Optional<Project> queryByAppIdAndSecretAndDelete(String appId , String appSecret){
        return projectRepo.findByAppIdAndAppSecretAndDeleted(appId , appSecret , false);
    }

    public boolean save(Project project){
        return projectRepo.save(project) != null;
    }

    public long count(){
        return projectRepo.count();
    }

    public Page<Project> findAll(Pageable pageable){
        return projectRepo.findAllByDeleted(pageable , false);
    }

    public Optional<Project> findByIdAndDeleted(Integer id, boolean deleted){
        return projectRepo.findByIdAndDeleted(id , deleted);
    }

    public Page<Project> findAll(Specification<Project> specification , Pageable pageable){
        return projectRepo.findAll(specification , pageable);
    }

    public long count(Specification<Project> specification){
        return projectRepo.count(specification);
    }

    public List<Project> findAllByIdAndDeleted(int[] ids , boolean deleted){
        List<Integer> integers = new ArrayList<>();
        for(int id : ids){
            integers.add(id);
        }
        return projectRepo.findAllByDeletedAndIdIn(deleted , integers);
    }

    public List<Project> saveAll(List<Project> projects){
        return projectRepo.saveAll(projects);
    }


    public List<Project> findAllForSelect(){
        return projectRepo.findAllByDeleted(false);
    }



}
