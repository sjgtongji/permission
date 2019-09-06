package com.duofuen.permission.controller;

import com.duofuen.permission.common.ErrorNum;
import com.duofuen.permission.controller.bean.CreateUserRequest;
import com.duofuen.permission.controller.bean.CreateUserResponse;
import com.duofuen.permission.controller.bean.LoginRequest;
import com.duofuen.permission.controller.bean.LoginResponse;
import com.duofuen.permission.domain.entity.Project;
import com.duofuen.permission.domain.entity.Role;
import com.duofuen.permission.domain.entity.User;
import com.duofuen.permission.service.ProjectService;
import com.duofuen.permission.service.RoleService;
import com.duofuen.permission.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping(path = "/rest/user")
public class UserRestController {
    private static Logger log = LogManager.getLogger();

    private final UserService userService;
    private final ProjectService projectService;
    private final RoleService roleService;
    @Autowired
    public UserRestController(UserService userService, ProjectService projectService, RoleService roleService) {
        this.userService = userService;
        this.projectService = projectService;
        this.roleService = roleService;
    }

    @Transactional
    @PostMapping("/login")
    @ResponseBody
    public LoginResponse login(@RequestBody LoginRequest request) {
        try {
            log.info("手机登录, {}", request);
            LoginResponse response =  userService.login(request);
            log.info("登陆成功！");
            return response;
        } catch (Exception e) {
            log.error("登陆失败！");
            log.error(e);
            LoginResponse response = new LoginResponse();
            response.setCode(-1);
            response.setMessage(e.getMessage());
            return response;
        }
    }

    @Transactional
    @PostMapping("/create")
    @ResponseBody
    public CreateUserResponse create(@RequestBody CreateUserRequest request) {
        try {
            log.info("新增用户", request);
            CreateUserResponse response = new CreateUserResponse();
            if(request.getProjectId() == null){
                response.setResult(ErrorNum.INVALID_PARAM_PJO_ID);
                return response;
            }
            if(request.getRoleId() == null){
                response.setResult(ErrorNum.INVALID_PARAM_ROLE_ID);
                return response;
            }
            if(request.getUserName() == null || request.getUserName().equals("")){
                response.setResult(ErrorNum.INVALID_PARAM_USERNAME);
                return response;
            }
            if(request.getPassword() == null || request.getPassword().equals("")){
                response.setResult(ErrorNum.INVALID_PARAM_PWD);
                return response;
            }
            Optional<Project> optionalProject = projectService.findById(request.getProjectId());
            if(!optionalProject.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_PJO_ID);
                return response;
            }
            Optional<Role> optionalRole = roleService.findById(request.getRoleId());
            if(!optionalRole.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_ROLE_ID);
                return response;
            }
            if(!optionalProject.get().getId().equals(optionalRole.get().getProjectId())){
                response.setResult(ErrorNum.INVALID_PARAM_PJO_ID_OR_ROLE_ID);
                return response;
            }
            Date date = new Date();
            User user = new User();
            user.setCreateTime(date.getTime());
            user.setUpdateTime(date.getTime());
            user.setProjectId(request.getProjectId());
            user.setRoleId(request.getRoleId());
            user.setUserName(request.getUserName());
            user.setPassword(request.getPassword());
            user.setEmail(request.getEmail());
            user.setPhone(request.getPhone());
            User result = userService.save(user);
            if(result != null){
                response.getData().setUserId(result.getId());
                log.info("新增用户成功！");
            }else{
                response.setResult(ErrorNum.UNKNOWN);
                log.info("新增用户失败！");
            }
            return response;
        } catch (Exception e) {
            log.error("新增用户失败！");
            log.error(e);
            CreateUserResponse response = new CreateUserResponse();
            response.setCode(ErrorNum.UNKNOWN.getCode());
            response.setMessage(e.getMessage());
            return response;
        }
    }
}
