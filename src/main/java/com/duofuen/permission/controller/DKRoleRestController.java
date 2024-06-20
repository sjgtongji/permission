package com.duofuen.permission.controller;

import com.alibaba.fastjson.JSON;
import com.duofuen.permission.common.ErrorNum;
import com.duofuen.permission.controller.bean.*;
import com.duofuen.permission.domain.entity.DKRole;
import com.duofuen.permission.domain.entity.DKStore;
import com.duofuen.permission.domain.entity.DKUser;
import com.duofuen.permission.service.DKRoleService;
import com.duofuen.permission.service.DKStoreService;
import com.duofuen.permission.service.DKUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping(path = "/rest/dkmenu")
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
            Optional<DKRole> optionalRole = dkRoleService.findByIdAndDeleted(dkUser.getDkRoleId() , false);
            if(!optionalRole.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_ROLE_ID);
                return response;
            }
            if(optionalRole.get().getPermission() < MAX_PERMISSION){
                response.setResult(ErrorNum.INVALID_PARAM_PERMISSION);
                return response;
            }
            Optional<DKStore> optionalDkStore = dkStoreService.findByIdAndDeleted(request.getStoreId() , false);
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
            Optional<DKRole> optionalRole = dkRoleService.findByIdAndDeleted(dkUser.getDkRoleId() , false);
            if(!optionalRole.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_ROLE_ID);
                return response;
            }
            if(optionalRole.get().getPermission() < MAX_PERMISSION){
                response.setResult(ErrorNum.INVALID_PARAM_PERMISSION);
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
}
