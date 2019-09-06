package com.duofuen.permission.controller;

import com.duofuen.permission.common.ErrorNum;
import com.duofuen.permission.controller.bean.*;
import com.duofuen.permission.domain.entity.Project;
import com.duofuen.permission.service.ProjectService;
import com.duofuen.permission.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/rest/project")
public class ProjectRestController {
    private static Logger log = LogManager.getLogger();

    private final ProjectService projectService;

    @Autowired
    public ProjectRestController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Transactional
    @PostMapping("/create")
    @ResponseBody
    public CreateProjectResponse create(@RequestBody CreateProjectRequest request) {
        try {
            log.info("新建项目", request);
            Date date = new Date();
            Project project = new Project();
            project.setCompanyName(request.getCompanyName());
            project.setName(request.getProjectName());
            project.setEmail(request.getEmail());
            project.setPhone(request.getPhone());
            project.setAppId(new Long(date.getTime()).toString() + projectService.count());
            project.setAppSecret(UUID.randomUUID().toString().replaceAll("-" , ""));
            project.setCreateTime(date.getTime());
            project.setUpdateTime(date.getTime());
            boolean success = projectService.save(project);
            CreateProjectResponse response = new CreateProjectResponse();
            if(success){
                response.setResult(ErrorNum.SUCCESS);
                log.info("新建项目成功！");
            }else{
                response.setResult(ErrorNum.FAIL);
                log.info("新建项目失败！");
            }
            return response;
        } catch (Exception e) {
            log.error("新建项目失败！");
            log.error(e);
            CreateProjectResponse response = new CreateProjectResponse();
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }

    @Transactional
    @GetMapping("/query")
    @ResponseBody
    public QueryProjectResponse query(QueryProjectRequest request) {
        try {
            log.info("查询项目", request);
            Page<Project> page = projectService.findAll(PageRequest.of(request.getPage(), request.getSize(), Sort.Direction.DESC, "createTime"));
            QueryProjectResponse response = new QueryProjectResponse();
            List<Project> projects = new ArrayList<>();
            for(Project item : page){
                projects.add(item);
            }
            response.getData().setList(projects);
            log.error("查询项目成功！");
            return response;
        } catch (Exception e) {
            log.error("查询项目失败！");
            log.error(e);
            QueryProjectResponse response = new QueryProjectResponse();
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }
}
