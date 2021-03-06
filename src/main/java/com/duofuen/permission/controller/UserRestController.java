package com.duofuen.permission.controller;

import com.alibaba.fastjson.JSON;
import com.duofuen.permission.common.ErrorNum;
import com.duofuen.permission.controller.bean.*;
import com.duofuen.permission.domain.entity.Menu;
import com.duofuen.permission.domain.entity.Project;
import com.duofuen.permission.domain.entity.Role;
import com.duofuen.permission.domain.entity.User;
import com.duofuen.permission.service.ProjectService;
import com.duofuen.permission.service.RoleService;
import com.duofuen.permission.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/rest/user")
@CrossOrigin
public class UserRestController {
    private static Logger log = LogManager.getLogger();
    private static String DEFAULT_PWD = "111111";
    private final UserService userService;
    private final ProjectService projectService;
    private final RoleService roleService;
    private final BCryptPasswordEncoder encoder;
    @Autowired
    public UserRestController(UserService userService, ProjectService projectService, RoleService roleService , BCryptPasswordEncoder encoder) {
        this.userService = userService;
        this.projectService = projectService;
        this.roleService = roleService;
        this.encoder = encoder;
    }

    @Transactional
    @PostMapping("/login")
    @ResponseBody
    public LoginResponse login(@RequestBody LoginRequest request) {
        try {
            log.info(JSON.toJSONString(request));
            return userService.login(request);
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
        CreateUserResponse response = new CreateUserResponse();
        try {
            log.info("新增用户", request);

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

            Optional<Project> optionalProject = projectService.findByIdAndDeleted(request.getProjectId() , false);
            if(!optionalProject.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_PJO_ID);
                return response;
            }
            Optional<Role> optionalRole = roleService.findByIdAndDeleted(request.getRoleId() , false);
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
            user.setProjectName(optionalProject.get().getName());
            user.setRoleId(request.getRoleId());
            user.setUserName(request.getUserName());
            user.setValid(true);
            user.setPassword(DEFAULT_PWD);
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
            response.setCode(ErrorNum.UNKNOWN.getCode());
            response.setMessage(e.getMessage());
            return response;
        }
    }
    @Transactional
    @PostMapping("/setRole")
    @ResponseBody
    public SetRoleResponse setRole(@RequestBody SetRoleRequest request){
        log.info("设置用户角色", request);
        SetRoleResponse response = new SetRoleResponse();
        if(request.getUserId() == null){
            response.setResult(ErrorNum.INVALID_PARAM_USER_ID);
            log.info("设置用户角色失败，userId参数错误");
            return response;
        }
        if(request.getRoleId() == null){
            response.setResult(ErrorNum.INVALID_PARAM_ROLE_ID);
            log.info("设置用户角色失败，roleId参数错误");
            return response;
        }

        Optional<User> userOptional = userService.queryById(request.getUserId());
        if(!userOptional.isPresent()){
            response.setResult(ErrorNum.INVALID_PARAM_USER_ID);
            log.info("设置用户角色失败，userId参数错误");
            return response;
        }

        Optional<Role> roleOptional = roleService.findByIdAndDeleted(request.getRoleId() , false);
        if(!roleOptional.isPresent()){
            response.setResult(ErrorNum.INVALID_PARAM_ROLE_ID);
            log.info("设置用户角色失败，roleId参数错误");
            return response;
        }
        User user = userOptional.get();
        Role role = roleOptional.get();
        if(user.getProjectId() == null || role.getProjectId() == null || !user.getProjectId().equals(role.getProjectId())){
            response.setResult(ErrorNum.INVALID_PARAM_USER_ID);
            log.info("设置用户角色失败，userId参数错误");
            return response;
        }

        user.setRole(role);
        user.setRoleId(role.getId());
        userService.save(user);
        response.setResult(ErrorNum.SUCCESS);
        log.info("设置用户角色完成");
        return response;

    }

    @Transactional
    @GetMapping("/query")
    @ResponseBody
    public QueryUserResponse query(QueryUserRequest request) {
        QueryUserResponse response = new QueryUserResponse();
        try {
            log.info("查询用户", request);
            log.info(JSON.toJSONString(request));

            List<User> users = new ArrayList<>();
            if(request.getCurrent() < 1 ){
                request.setCurrent(1);
            }
            if(request.getPageSize() < 0){
                request.setPageSize(20);
            }
            Page<User> page = null;
            Pageable pageable = PageRequest.of(request.getCurrent()-1, request.getPageSize(), Sort.Direction.DESC, "createTime");
            Specification<User> specification = new Specification<User>() {
                @Override
                public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicates = new ArrayList<>();
                    if(request.getUserName() != null && !request.getUserName().equals("")){
                        predicates.add(criteriaBuilder.like(root.get("userName").as(String.class), "%" + request.getUserName() + "%"));
                    }

                    if(request.getProjectName() != null && !request.getProjectName().equals("")){
                        predicates.add(criteriaBuilder.like(root.get("projectName").as(String.class), "%" +request.getProjectName()+ "%"));
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
                    predicates.add(criteriaBuilder.isFalse(root.get("deleted")));
                    Predicate[] p = new Predicate[predicates.size()];
                    return criteriaBuilder.and(predicates.toArray(p));
                }
            };
            page = userService.findAll(specification , pageable);

            for(User item : page){
                users.add(item);
            }
            response.getData().setData(users);
            response.getData().setCurrent(request.getCurrent());
            response.getData().setPageSize(request.getPageSize());
            response.getData().setTotal((int) userService.count(specification));
            response.getData().setSuccess(true);
            log.error("查询用户成功！");
            return response;
        } catch (Exception e) {
            log.error("查询用户失败！");
            log.error(e);
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }

    @Transactional
    @PostMapping("/modify")
    @ResponseBody
    public ModifyUserResponse modify(@RequestBody ModifyUserRequest request) {
        ModifyUserResponse response = new ModifyUserResponse();
        try {
            log.info("修改用户, {}", request);
            log.info(JSON.toJSONString(request));

            if(request.getId() == null || request.getId() <= 0){
                log.error("修改用户失败！");
                response.setResult(ErrorNum.INVALID_PARAM_USER_ID);
                return response;
            }
            Optional<Project> optionalProject = projectService.findByIdAndDeleted(request.getProjectId() , false);
            if(!optionalProject.isPresent()){
                log.error("修改用户失败！");
                response.setResult(ErrorNum.INVALID_PARAM_PJO_ID);
                return response;
            }
            if(!optionalProject.get().getId().equals(request.getProjectId())){
                log.error("修改用户失败！");
                response.setResult(ErrorNum.INVALID_PARAM_PJO_ID);
                return response;
            }
            if(request.getUserName() == null || request.getUserName().equals("")){
                log.error("修改用户失败！");
                response.setResult(ErrorNum.INVALID_PARAM_USERNAME);
                return response;
            }
            if(request.getRoleId() == null || request.getRoleId() <= 0){
                log.error("修改用户失败！");
                response.setResult(ErrorNum.INVALID_PARAM_ROLE_ID);
                return response;
            }
//            List<Role> exists = roleService.findAllByCodeAndDeleted(request.getCode() , false);
//            if(exists != null && exists.size() > 0){
//                if(exists.size() == 1){
//                    if(!exists.get(0).getId().equals(request.getId())){
//                        log.error("修改用户失败！");
//                        response.setResult(ErrorNum.INVALID_PARAM_ROLE_CODE_EXIST);
//                        return response;
//                    }
//                }else{
//                    log.error("修改用户失败！");
//                    response.setResult(ErrorNum.INVALID_PARAM_ROLE_CODE_EXIST);
//                    return response;
//                }
//            }

            Optional<Role> roleOptional = roleService.findByIdAndDeleted(request.getId() , false);
            if(!roleOptional.isPresent()){
                log.error("修改用户失败！");
                response.setResult(ErrorNum.INVALID_PARAM_ROLE_ID);
                return response;
            }
            Optional<User> optionalUser = userService.findByIdAndDeleted(request.getId() , false);
            if(!optionalUser.isPresent()){
                log.error("修改用户失败！");
                response.setResult(ErrorNum.INVALID_PARAM_USER_ID);
                return response;
            }
            Date date = new Date();
            User current = optionalUser.get();
            current.setValid(request.isValid());
            current.setEmail(request.getEmail());
            current.setPhone(request.getPhone());
            current.setUpdateTime(date.getTime());
            User result = userService.save(current);
            if(result != null ){
                response.getData().setUserId(result.getId());
                log.info("修改用户成功！");
            }else{
                response.setResult(ErrorNum.UNKNOWN);
                log.error("修改用户失败！");
            }
            return response;
        } catch (Exception e) {
            log.error("修改用户失败！");
            log.error(e);
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }

    @Transactional
    @PostMapping("/batchValid")
    @ResponseBody
    public EmptyResponse batchValid(@RequestBody BatchValidUserRequest request) {
        EmptyResponse response = new EmptyResponse();
        try {
            log.info("批量启用用户", request);
            log.info(JSON.toJSONString(request));
            if(request.getIds() == null || request.getIds().size() == 0){
                log.error("批量启用用户失败！");
                response.setResult(ErrorNum.INVALID_PARAM_USER_ID);
                return response;
            }
            List<User> users = userService.findAllByIdInAndDeleted(request.getIds() , false);
            if(users == null || users.size() == 0){
                response.setResult(ErrorNum.INVALID_PARAM_USER_ID);
                return response;
            }
            for(User user : users){
                user.setValid(true);
            }
            List<User> results = userService.saveAll(users);
            if(results != null && results.size() == users.size()){
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
    public EmptyResponse batchUnvalid(@RequestBody BatchValidUserRequest request) {
        EmptyResponse response = new EmptyResponse();
        try {
            log.info("批量禁用用户", request);
            log.info(JSON.toJSONString(request));
            if(request.getIds() == null || request.getIds().size() == 0){
                log.error("批量禁用用户失败！");
                response.setResult(ErrorNum.INVALID_PARAM_USER_ID);
                return response;
            }
            List<User> users = userService.findAllByIdInAndDeleted(request.getIds() , false);
            if(users == null || users.size() == 0){
                response.setResult(ErrorNum.INVALID_PARAM_USER_ID);
                return response;
            }
            for(User user : users){
                user.setValid(false);
            }
            List<User> results = userService.saveAll(users);
            if(results != null && results.size() == users.size()){
                response.setResult(ErrorNum.SUCCESS);
            }else{
                response.setResult(ErrorNum.FAIL);
            }
            log.info("批量禁用成功！");
            return response;
        } catch (Exception e) {
            log.error("批量禁用失败！");
            log.error(e);
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }

    @Transactional
    @PostMapping("/delete")
    @ResponseBody
    public EmptyResponse delete(@RequestBody DeleteUserRequest request) {
        EmptyResponse response = new EmptyResponse();
        try {
            log.info("删除用户", request);
            log.info(JSON.toJSONString(request));
            if(request.getId() < 0){
                response.setResult(ErrorNum.INVALID_PARAM_USER_ID);
                return response;
            }
            Optional<User> userOptional = userService.findByIdAndDeleted(request.getId() , false);

            if(!userOptional.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_USER_ID);
                return response;
            }
            User user = userOptional.get();
            user.setDeleted(true);
            response.setResult(userService.save(user) != null ? ErrorNum.SUCCESS : ErrorNum.FAIL);
            log.info("删除用户成功！");
            return response;
        } catch (Exception e) {
            log.error("删除用户失败！");
            log.error(e);
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }

    @Transactional
    @PostMapping("/batchDelete")
    @ResponseBody
    public EmptyResponse batchDelete(@RequestBody BatchDeleteUserRequest request) {
        EmptyResponse response = new EmptyResponse();
        try {
            log.info("批量删除用户", request);
            log.info(JSON.toJSONString(request));
            if(request.getIds() == null || request.getIds().size() == 0){
                response.setResult(ErrorNum.INVALID_PARAM_USER_ID);
                return response;
            }
            List<User> users = userService.findAllByIdInAndDeleted(request.getIds() , false);
            if(users == null || users.size() == 0){
                response.setResult(ErrorNum.INVALID_PARAM_USER_ID);
                return response;
            }
            for(User user : users){
                user.setDeleted(true);
            }
            List<User> results = userService.saveAll(users);
            if(results != null && results.size() == users.size()){
                response.setResult(ErrorNum.SUCCESS);
            }else{
                response.setResult(ErrorNum.FAIL);
            }
            log.info("批量删除用户成功！");
            return response;
        } catch (Exception e) {
            log.error("批量删除用户失败！");
            log.error(e);
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }
}
