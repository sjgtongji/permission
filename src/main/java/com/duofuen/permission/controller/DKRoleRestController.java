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
@RequestMapping(path = "/rest/dkrole")
public class DKRoleRestController {
    private static Logger log = LogManager.getLogger();
    private final DKStoreService dkStoreService;
    private final DKUserService dkUserService;
    private final DKRoleService dkRoleService;
    private final int MAX_PERMISSION = 100;
    public DKRoleRestController(DKStoreService dkStoreService,DKUserService dkUserService,
                                DKRoleService dkRoleService) {
        this.dkStoreService = dkStoreService;
        this.dkUserService = dkUserService;
        this.dkRoleService = dkRoleService;
    }

    @Transactional
    @PostMapping("/create")
    @ResponseBody
    public CreateDKRoleResponse create(@RequestBody CreateDKRoleRequest request) {
        CreateDKRoleResponse response = new CreateDKRoleResponse();
        try {
            log.info("新增角色", request);
            log.info(JSON.toJSONString(request));
            if(request.getToken() == null || "".equals(request.getToken())){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            if(request.getName() == null || "".equals(request.getName())){
                response.setResult(ErrorNum.INVALID_PARAM_NAME);
                return response;
            }
            if(request.getPermission() == null || request.getPermission() <= 0){
                response.setResult(ErrorNum.INVALID_PARAM_PERMISSION);
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
            if(!dkUserService.isAdmin(request.getToken())){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            DKUser dkUser = dkUserService.findUserByToken(request.getToken());
            if(dkUser == null){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
//            Optional<DKRole> optionalRole = dkRoleService.findByIdAndDeleted(dkUser.getDkRoleId() , false);
//            if(!optionalRole.isPresent()){
//                response.setResult(ErrorNum.INVALID_PARAM_ROLE_ID);
//                return response;
//            }
//            if(optionalRole.get().getPermission() < MAX_PERMISSION){
//                response.setResult(ErrorNum.INVALID_PARAM_PERMISSION);
//                return response;
//            }
            Optional<DKStore> optionalDkStore = dkStoreService.findByIdAndDeleted(request.getDkStoreId() , false);
            if(!optionalDkStore.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_STORE_ID);
                return response;
            }

            Date date = new Date();
            DKRole role = new DKRole();
            role.setCreateTime(date.getTime());
            role.setUpdateTime(date.getTime());
            role.setDkStoreId(optionalDkStore.get().getId());
            role.setDkStoreName(optionalDkStore.get().getName());
            role.setDkStore(optionalDkStore.get());
            role.setName(request.getName());
            role.setPermission(request.getPermission());
            DKRole result = dkRoleService.save(role);
            if(result != null){
                response.getData().setDkRoleId(result.getId());
                log.info("新增角色成功！");
            }else{
                response.setResult(ErrorNum.UNKNOWN);
                log.info("新增角色失败！");
            }
            return response;
        }catch (Exception e){
            log.error("新增角色失败！");
            log.error(e);
            response.setResult(ErrorNum.UNKNOWN);
            return response;
        }
    }

    @Transactional
    @PostMapping("/modify")
    @ResponseBody
    public ModifyDKRoleResponse modify(@RequestBody ModifyDKRoleRequest request) {
        ModifyDKRoleResponse response = new ModifyDKRoleResponse();
        try {
            log.info("修改角色, {}", request);
            log.info(JSON.toJSONString(request));

            if(request.getId() == null || request.getId() <= 0){
                log.error("修改角色失败！");
                response.setResult(ErrorNum.INVALID_PARAM_ROLE_ID);
                return response;
            }
            if(request.getToken() == null || "".equals(request.getToken())){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            if(request.getName() == null || "".equals(request.getName())){
                response.setResult(ErrorNum.INVALID_PARAM_NAME);
                return response;
            }
            if(request.getPermission() == null || request.getPermission() <= 0){
                response.setResult(ErrorNum.INVALID_PARAM_PERMISSION);
                return response;
            }
            if(request.getStoreId() == null || request.getStoreId() <= 0){
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
            Optional<DKRole> optionalRole = dkRoleService.findByIdAndDeleted(dkUser.getDkRoleId() , false);
            if(!optionalRole.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_ROLE_ID);
                return response;
            }
            Optional<DKStore> optionalDkStore = dkStoreService.findByIdAndDeleted(request.getStoreId() , false);
            if(!optionalDkStore.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_STORE_ID);
                return response;
            }

            Optional<DKRole> roleOptional = dkRoleService.findByIdAndDeleted(request.getId() , false);
            if(!roleOptional.isPresent()){
                log.error("修改角色失败！");
                response.setResult(ErrorNum.INVALID_PARAM_ROLE_ID);
                return response;
            }
            DKRole role = roleOptional.get();

            Date date = new Date();
            role.setUpdateTime(date.getTime());
            role.setDkStoreId(optionalDkStore.get().getId());
            role.setDkStoreName(optionalDkStore.get().getName());
            role.setDkStore(optionalDkStore.get());
            role.setName(request.getName());
            role.setPermission(request.getPermission());
            DKRole result = dkRoleService.save(role);
            if(result != null ){
                response.getData().setId(result.getId());
                log.info("修改角色成功！");
            }else{
                response.setResult(ErrorNum.UNKNOWN);
                log.error("修改角色失败！");
            }
            return response;
        } catch (Exception e) {
            log.error("修改角色失败！");
            log.error(e);
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }

    @Transactional
    @GetMapping("/queryForSelect")
    @ResponseBody
    public QueryDKRoleForSelectResponse queryForSelect(QueryDKRoleForSelectRequest request) {
        QueryDKRoleForSelectResponse response = new QueryDKRoleForSelectResponse();
        try {
            log.info("查询角色", request);
            log.info(JSON.toJSONString(request));
            if(request.getDkStoreId() == null || request.getDkStoreId() <= 0){
                log.error("查询角色失败！");
                response.setResult(ErrorNum.INVALID_PARAM_STORE_ID);
                return response;
            }
            Optional<DKStore> optionalProject = dkStoreService.findByIdAndDeleted(request.getDkStoreId() , false);
            if(!optionalProject.isPresent()){
                log.error("查询角色失败！");
                response.setResult(ErrorNum.INVALID_PARAM_STORE_ID);
                return response;
            }
            List<DKRole> roles = dkRoleService.findAllByDkStoreIdAndDeleted(request.getDkStoreId() , false);
            response.getData().setData(roles);
            log.error("查询角色成功！");
            return response;
        } catch (Exception e) {
            log.error("查询角色失败！");
            log.error(e);
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }

    @Transactional
    @GetMapping("/query")
    @ResponseBody
    public QueryDKRoleResponse query(QueryDKRoleRequest request) {
        QueryDKRoleResponse response = new QueryDKRoleResponse();
        try {
            log.info("查询角色", request);
            log.info(JSON.toJSONString(request));

            List<DKRole> roles = new ArrayList<>();
            if(request.getCurrent() < 1 ){
                request.setCurrent(1);
            }
            if(request.getPageSize() < 0){
                request.setPageSize(20);
            }
            Page<DKRole> page = null;
            Pageable pageable = PageRequest.of(request.getCurrent()-1, request.getPageSize(), Sort.Direction.DESC, "createTime");
            Specification<DKRole> specification = new Specification<DKRole>() {
                @Override
                public Predicate toPredicate(Root<DKRole> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicates = new ArrayList<>();
                    if(request.getName() != null && !request.getName().equals("")){
                        predicates.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + request.getName() + "%"));
                    }

                    if(request.getPermission() != null && request.getPermission() > 0){
                        predicates.add(criteriaBuilder.equal(root.get("permission").as(Integer.class), "%" +request.getPermission()+ "%"));
                    }

                    if(request.getDkStoreName() != null && !request.getDkStoreName().equals("")){
                        predicates.add(criteriaBuilder.like(root.get("dkStoreName").as(String.class), "%" +request.getDkStoreName()+ "%"));
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
            page = dkRoleService.findAll(specification , pageable);

            for(DKRole item : page){
                roles.add(item);
            }
            response.getData().setData(roles);
            response.getData().setCurrent(request.getCurrent());
            response.getData().setPageSize(request.getPageSize());
            response.getData().setTotal((int) dkRoleService.count(specification));
            response.getData().setSuccess(true);
            log.error("查询角色成功！");
            return response;
        } catch (Exception e) {
            log.error("查询角色失败！");
            log.error(e);
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }
}
