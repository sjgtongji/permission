package com.duofuen.permission.controller;

import com.duofuen.permission.common.ErrorNum;
import com.duofuen.permission.controller.bean.*;
import com.duofuen.permission.domain.entity.Project;
import com.duofuen.permission.domain.entity.Role;
import com.duofuen.permission.service.ProjectService;
import com.duofuen.permission.service.RoleService;
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
import java.util.Optional;

@RestController
@RequestMapping(path = "/rest/role")
public class RoleRestController {
    private static Logger log = LogManager.getLogger();

    private final RoleService roleService;
    private final ProjectService projectService;
    @Autowired
    public RoleRestController(RoleService roleService, ProjectService projectService) {
        this.roleService = roleService;
        this.projectService = projectService;
    }

    @Transactional
    @PostMapping("/create")
    @ResponseBody
    public CreateRoleResponse create(@RequestBody CreateRoleRequest request) {
        try {
            log.info("创建角色, {}", request);
            CreateRoleResponse response = new CreateRoleResponse();
            if(request.getAppId() == null || request.getAppId().equals("")){
                log.error("创建角色失败！");
                response.setResult(ErrorNum.EMPTY_APPID);
                return response;
            }

            if(request.getAppSecret() == null || request.getAppSecret().equals("")){
                log.error("创建角色失败！");
                response.setResult(ErrorNum.EMTPY_APPSECRET);
                return response;
            }

            if(request.getName() == null || request.getName().equals("")){
                log.error("创建角色失败！");
                response.setResult(ErrorNum.INVALID_PARAM_NAME);
                return response;
            }
            Optional<Project> optionalProject = projectService.queryByAppIdAndSecretAndDelete(request.getAppId() , request.getAppSecret());
            if(!optionalProject.isPresent()){
                log.error("创建角色失败！");
                response.setResult(ErrorNum.INVALID_APPID_OR_APPSECRET);
                return response;
            }
            Date date = new Date();
            Role role = new Role();
            role.setName(request.getName());
            role.setProjectId(optionalProject.get().getId());
            role.setProject(optionalProject.get());
            role.setCreateTime(date.getTime());
            role.setUpdateTime(date.getTime());
            Role result = roleService.save(role);
            if(result != null ){
                response.getData().setRoleId(result.getId());
                log.info("创建角色成功！");
            }else{
                response.setResult(ErrorNum.UNKNOWN);
                log.error("创建角色失败！");
            }
            return response;
        } catch (Exception e) {
            log.error("创建角色失败！");
            log.error(e);
            CreateRoleResponse response = new CreateRoleResponse();
            response.setCode(ErrorNum.UNKNOWN.getCode());
            response.setMessage(e.getMessage());
            return response;
        }
    }

    @Transactional
    @GetMapping("/query")
    @ResponseBody
    public QueryRoleResponse query(QueryRoleRequest request) {
        try {
            log.info("查询角色", request);
            Page<Role> page = roleService.findAll(PageRequest.of(request.getPage(), request.getSize(), Sort.Direction.DESC, "createTime"));
            QueryRoleResponse response = new QueryRoleResponse();
            List<Role> roles = new ArrayList<>();
            for(Role item : page){
                roles.add(item);
            }
            response.getData().setList(roles);
            log.error("查询角色成功！");
            return response;
        } catch (Exception e) {
            log.error("查询角色失败！");
            log.error(e);
            QueryRoleResponse response = new QueryRoleResponse();
            response.setResult(ErrorNum.UNKNOWN);
            return response;
        }
    }
}
