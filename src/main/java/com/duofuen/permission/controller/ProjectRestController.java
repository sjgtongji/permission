package com.duofuen.permission.controller;

import com.alibaba.fastjson.JSON;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@RestController
@RequestMapping(path = "/rest/project")
@CrossOrigin
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
            project.setName(request.getName());
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
            log.info(JSON.toJSONString(request));
            QueryProjectResponse response = new QueryProjectResponse();
            List<Project> projects = new ArrayList<>();
            if(request.getCurrent() < 1 ){
                request.setCurrent(1);
            }
            if(request.getPageSize() < 0){
                request.setPageSize(20);
            }
            Page<Project> page = null;
            Pageable pageable = PageRequest.of(request.getCurrent()-1, request.getPageSize(), Sort.Direction.DESC, "createTime");
            Specification<Project> specification = new Specification<Project>() {
                @Override
                public Predicate toPredicate(Root<Project> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicates = new ArrayList<>();
                    if(request.getName() != null && !request.getName().equals("")){
                        predicates.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + request.getName() + "%"));
                    }

                    if(request.getCompanyName() != null && !request.getCompanyName().equals("")){
                        predicates.add(criteriaBuilder.like(root.get("companyName").as(String.class), "%" +request.getCompanyName()+ "%"));
                    }

                    if(request.getPhone() != null && !request.getPhone().equals("")){
                        predicates.add(criteriaBuilder.like(root.get("phone").as(String.class), "%" +request.getPhone()+ "%"));
                    }

                    if(request.getEmail() != null && !request.getEmail().equals("")){
                        predicates.add(criteriaBuilder.like(root.get("email").as(String.class), "%" +request.getEmail()+ "%"));
                    }
                    if(request.getValid() != null && !request.getValid().equals("")){
                        if(request.getValid().equals("true")){
                            predicates.add(criteriaBuilder.isTrue(root.get("valid")));
                        }else if(request.getValid().equals("false")){
                            predicates.add(criteriaBuilder.isFalse(root.get("valid")));
                        }
                    }
                    Predicate[] p = new Predicate[predicates.size()];
                    return criteriaBuilder.and(predicates.toArray(p));
                }
            };
            page = projectService.findAll(specification , pageable);

            for(Project item : page){
                projects.add(item);
            }
            response.getData().setData(projects);
            response.getData().setCurrent(request.getCurrent());
            response.getData().setPageSize(request.getPageSize());
            response.getData().setTotal((int) projectService.count(specification));
            response.getData().setSuccess(true);
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

    @Transactional
    @GetMapping("/queryForSelect")
    @ResponseBody
    public QueryProjectForSelectResponse queryForSelect(QueryProjectForSelectRequest request) {
        QueryProjectForSelectResponse response = new QueryProjectForSelectResponse();
        try {
            log.info("查询项目", request);
            log.info(JSON.toJSONString(request));
            List<Project> projects = projectService.findAllForSelect();
            response.getData().setData(projects);
            log.error("查询项目成功！");
            return response;
        } catch (Exception e) {
            log.error("查询项目失败！");
            log.error(e);
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }

    @Transactional
    @PostMapping("/modify")
    @ResponseBody
    public ModifyProjectResponse modify(@RequestBody ModifyProjectRequest request) {
        ModifyProjectResponse response = new ModifyProjectResponse();
        try {
            log.info("修改项目", request);
            log.info(JSON.toJSONString(request));
            Date date = new Date();
            if(request.getId() < 0 ){
                response.setResult(ErrorNum.INVALID_PARAM_PJO_ID);
                return response;
            }
            Optional<Project> projectOptional = projectService.findById(request.getId());
            if(!projectOptional.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_PJO_ID);
                return response;
            }
            Project project = projectOptional.get();
            project.setCompanyName(request.getCompanyName());
            project.setName(request.getName());
            project.setEmail(request.getEmail());
            project.setPhone(request.getPhone());
            project.setUpdateTime(date.getTime());
            project.setValid(request.isValid());
            boolean success = projectService.save(project);
            response.getData().setId(project.getId());
            if(success){

                response.setResult(ErrorNum.SUCCESS);
                log.info("修改项目成功！");
            }else{
                response.setResult(ErrorNum.FAIL);
                log.info("修改项目失败！");
            }
            return response;
        } catch (Exception e) {
            log.error("修改项目失败！");
            log.error(e);
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }

    @Transactional
    @PostMapping("/batchValid")
    @ResponseBody
    public EmptyResponse batchValid(@RequestBody BatchValidProjectRequest request) {
        EmptyResponse response = new EmptyResponse();
        try {
            log.info("批量启用项目", request);
            log.info(JSON.toJSONString(request));
            List<Project> projects = projectService.findAllByIds(request.getIds());
            if(projects == null || projects.size() == 0){
                response.setResult(ErrorNum.INVALID_PARAM_PJO_ID);
                return response;
            }
            for(Project project : projects){
                project.setValid(true);
            }
            List<Project> results = projectService.saveAll(projects);
            if(results != null && results.size() == projects.size()){
                response.setResult(ErrorNum.SUCCESS);
            }else{
                response.setResult(ErrorNum.FAIL);
            }
            log.info("批量启用成功！");
            return response;
        } catch (Exception e) {
            log.error("批量启用失败！");
            log.error(e);
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }

    @Transactional
    @PostMapping("/batchUnvalid")
    @ResponseBody
    public EmptyResponse batchUnvalid(@RequestBody BatchValidProjectRequest request) {
        EmptyResponse response = new EmptyResponse();
        try {
            log.info("批量禁用项目", request);
            log.info(JSON.toJSONString(request));
            List<Project> projects = projectService.findAllByIds(request.getIds());
            if(projects == null || projects.size() == 0){
                response.setResult(ErrorNum.INVALID_PARAM_PJO_ID);
                return response;
            }
            for(Project project : projects){
                project.setValid(false);
            }
            List<Project> results = projectService.saveAll(projects);
            if(results != null && results.size() == projects.size()){
                response.setResult(ErrorNum.SUCCESS);
            }else{
                response.setResult(ErrorNum.FAIL);
            }
            log.info("批量启用成功！");
            return response;
        } catch (Exception e) {
            log.error("批量禁用失败！");
            log.error(e);
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }
}
