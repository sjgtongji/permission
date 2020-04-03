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

import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectRepo projectRepo;

    @Autowired
    public ProjectService(ProjectRepo projectRepo) {
        this.projectRepo = projectRepo;
    }

    public Optional<Project> queryByAppIdAndSecret(String appId , String appSecret){
        return projectRepo.findByAppIdAndAppSecret(appId , appSecret);
    }

    public boolean save(Project project){
        return projectRepo.save(project) != null;
    }

    public long count(){
        return projectRepo.count();
    }

    public Page<Project> findAll(Pageable pageable){
        return projectRepo.findAll(pageable);
    }

    public Optional<Project> findById(Integer id){
        return projectRepo.findById(id);
    }

    public Page<Project> findAll(Specification<Project> specification , Pageable pageable){
        return projectRepo.findAll(specification , pageable);
    }

    public long count(Specification<Project> specification){
        return projectRepo.count(specification);
    }

}
