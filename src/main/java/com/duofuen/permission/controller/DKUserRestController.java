package com.duofuen.permission.controller;

import com.alibaba.fastjson.JSON;
import com.duofuen.permission.common.ErrorNum;
import com.duofuen.permission.controller.bean.*;
import com.duofuen.permission.domain.entity.*;
import com.duofuen.permission.service.DKRoleService;
import com.duofuen.permission.service.DKStoreService;
import com.duofuen.permission.service.DKUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/rest/dkuser")
public class DKUserRestController {

    private static Logger log = LogManager.getLogger();
    private final DKStoreService dkStoreService;
    private final DKUserService dkUserService;
    private final DKRoleService dkRoleService;
    private final int MAX_PERMISSION = 100;
    private static String DEFAULT_PWD = "111111";
    public DKUserRestController(DKStoreService dkStoreService,DKUserService dkUserService,
                                 DKRoleService dkRoleService) {
        this.dkStoreService = dkStoreService;
        this.dkUserService = dkUserService;
        this.dkRoleService = dkRoleService;
    }
    @Transactional
    @PostMapping("/login")
    @ResponseBody
    public DKLoginResponse login(@RequestBody DKLoginRequest request) {
        try {
            log.info(JSON.toJSONString(request));
            return dkUserService.login(request);
        } catch (Exception e) {
            log.error("登陆失败！");
            log.error(e);
            DKLoginResponse response = new DKLoginResponse();
            response.setCode(-1);
            response.setMessage(e.getMessage());
            return response;
        }
    }
    @Transactional
    @PostMapping("/create")
    @ResponseBody
    public CreateDKUserResponse create(@RequestBody CreateDKUserRequest request) {
        CreateDKUserResponse response = new CreateDKUserResponse();
        try {
            log.info("新增用户", request);
            log.info(JSON.toJSONString(request));
            if(request.getToken() == null || "".equals(request.getToken())){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            if(request.getDkRoleId() == null || request.getDkRoleId() <= 0){
                response.setResult(ErrorNum.INVALID_PARAM_ROLE_ID);
                return response;
            }
            if(request.getUserName() == null || request.getUserName().equals("")){
                response.setResult(ErrorNum.INVALID_PARAM_USERNAME);
                return response;
            }
            if(request.getDkStoreId() == null || request.getDkStoreId() <= 0){
                response.setResult(ErrorNum.INVALID_PARAM_STORE_ID);
                return response;
            }
            if(!dkUserService.tokenValid(request.getToken())){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            DKUser dkUser = dkUserService.findUserByToken(request.getToken());
            if(dkUser == null){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            if(!dkUserService.isAdmin(request.getToken())){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            Optional<DKRole> optionalRole = dkRoleService.findByIdAndDeleted(request.getDkRoleId() , false);
            if(!optionalRole.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_ROLE_ID);
                return response;
            }

            Optional<DKStore> optionalDkStore = dkStoreService.findByIdAndDeleted(request.getDkStoreId() , false);
            if(!optionalDkStore.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_STORE_ID);
                return response;
            }

            Date date = new Date();
            DKUser user = new DKUser();
            user.setCreateTime(date.getTime());
            user.setUpdateTime(date.getTime());
            user.setDkRoleId(request.getDkRoleId());
            user.setDkRoleName(optionalRole.get().getName());
            user.setDkRole(optionalRole.get());
            user.setDkStoreId(optionalDkStore.get().getId());
            user.setDkStoreName(optionalDkStore.get().getName());
            user.setDkStore(optionalDkStore.get());
            user.setUserName(request.getUserName());
            user.setPassword(DEFAULT_PWD);
            user.setEmail(request.getEmail());
            user.setPhone(request.getPhone());
            DKUser result = dkUserService.save(user);
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
    @PostMapping("/modify")
    @ResponseBody
    public ModifyDKUserResponse modify(@RequestBody ModifyDKUserRequest request) {
        ModifyDKUserResponse response = new ModifyDKUserResponse();
        try {
            log.info("修改用户", request);
            log.info(JSON.toJSONString(request));
            if(request.getId() == null || request.getId() <= 0){
                response.setResult(ErrorNum.INVALID_PARAM_USER_ID);
                return response;
            }
            if(request.getToken() == null || "".equals(request.getToken())){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            if(request.getDkRoleId() == null || request.getDkRoleId() <= 0){
                response.setResult(ErrorNum.INVALID_PARAM_ROLE_ID);
                return response;
            }
            if(request.getUserName() == null || request.getUserName().equals("")){
                response.setResult(ErrorNum.INVALID_PARAM_USERNAME);
                return response;
            }
            if(request.getDkStoreId() == null || request.getDkStoreId() <= 0){
                response.setResult(ErrorNum.INVALID_PARAM_STORE_ID);
                return response;
            }
            if(!dkUserService.tokenValid(request.getToken())){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            DKUser dkUser = dkUserService.findUserByToken(request.getToken());
            if(dkUser == null){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            if(!dkUserService.isAdmin(request.getToken())){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            Optional<DKRole> optionalRole = dkRoleService.findByIdAndDeleted(request.getDkRoleId() , false);
            if(!optionalRole.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_ROLE_ID);
                return response;
            }
            Optional<DKStore> optionalDkStore = dkStoreService.findByIdAndDeleted(request.getDkStoreId() , false);
            if(!optionalDkStore.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_STORE_ID);
                return response;
            }
            if(!optionalRole.get().getDkStoreId().equals(request.getDkStoreId())){
                response.setResult(ErrorNum.INVALID_PARAM_STORE_ID);
                return response;
            }
            Optional<DKUser> userOptional = dkUserService.findByIdAndDeleted(request.getId() , false);
            if(!userOptional.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_USER_ID);
                return response;
            }

            Date date = new Date();
            DKUser user = userOptional.get();
            user.setUpdateTime(date.getTime());
            user.setDkRoleId(request.getDkRoleId());
            user.setDkRoleName(optionalRole.get().getName());
            user.setDkRole(optionalRole.get());
            user.setDkStoreId(optionalDkStore.get().getId());
            user.setDkStoreName(optionalDkStore.get().getName());
            user.setDkStore(optionalDkStore.get());
            user.setUserName(request.getUserName());
            user.setEmail(request.getEmail());
            user.setPhone(request.getPhone());
            DKUser result = dkUserService.save(user);
            if(result != null){
                response.getData().setUserId(result.getId());
                log.info("修改用户成功！");
            }else{
                response.setResult(ErrorNum.UNKNOWN);
                log.info("修改用户失败！");
            }
            return response;
        } catch (Exception e) {
            log.error("修改用户失败！");
            log.error(e);
            response.setCode(ErrorNum.UNKNOWN.getCode());
            response.setMessage(e.getMessage());
            return response;
        }
    }

    @Transactional
    @GetMapping("/query")
    @ResponseBody
    public QueryDKUserResponse query(QueryDKUserRequest request) {
        QueryDKUserResponse response = new QueryDKUserResponse();
        try {
            log.info("查询用户", request);
            log.info(JSON.toJSONString(request));

            List<DKUser> users = new ArrayList<>();
            if(request.getCurrent() < 1 ){
                request.setCurrent(1);
            }
            if(request.getPageSize() < 0){
                request.setPageSize(20);
            }
            Page<DKUser> page = null;
            Pageable pageable = PageRequest.of(request.getCurrent()-1, request.getPageSize(), Sort.Direction.DESC, "createTime");
            Specification<DKUser> specification = new Specification<DKUser>() {
                @Override
                public Predicate toPredicate(Root<DKUser> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicates = new ArrayList<>();
                    if(request.getUserName() != null && !request.getUserName().equals("")){
                        predicates.add(criteriaBuilder.like(root.get("userName").as(String.class), "%" + request.getUserName() + "%"));
                    }

                    if(request.getStoreName() != null && !request.getStoreName().equals("")){
                        predicates.add(criteriaBuilder.like(root.get("dkStoretName").as(String.class), "%" +request.getStoreName()+ "%"));
                    }

                    if(request.getPhone() != null && !request.getPhone().equals("")){
                        predicates.add(criteriaBuilder.like(root.get("phone").as(String.class), "%" +request.getPhone()+ "%"));
                    }

                    if(request.getEmail() != null && !request.getEmail().equals("")){
                        predicates.add(criteriaBuilder.like(root.get("email").as(String.class), "%" +request.getEmail()+ "%"));
                    }
//                    if(request.getValid() != null && !request.getValid().equals("")){
//                        if(request.getValid().equals("true")){
//                            predicates.add(criteriaBuilder.isTrue(root.get("valid")));
//                        }else if(request.getValid().equals("false")){
//                            predicates.add(criteriaBuilder.isFalse(root.get("valid")));
//                        }
//                    }
                    predicates.add(criteriaBuilder.isFalse(root.get("deleted")));
                    Predicate[] p = new Predicate[predicates.size()];
                    return criteriaBuilder.and(predicates.toArray(p));
                }
            };
            page = dkUserService.findAll(specification , pageable);

            for(DKUser item : page){
                users.add(item);
            }
            response.getData().setData(users);
            response.getData().setCurrent(request.getCurrent());
            response.getData().setPageSize(request.getPageSize());
            response.getData().setTotal((int) dkUserService.count(specification));
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
}
