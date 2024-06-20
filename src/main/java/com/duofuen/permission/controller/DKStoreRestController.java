package com.duofuen.permission.controller;

import com.duofuen.permission.common.ErrorNum;
import com.duofuen.permission.controller.bean.CreateDKStoreRequest;
import com.duofuen.permission.controller.bean.CreateDKStoreResponse;
import com.duofuen.permission.controller.bean.ModifyDKStoreRequest;
import com.duofuen.permission.controller.bean.ModifyDKStoreResponse;
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
@RequestMapping(path = "/rest/dkstore")
@CrossOrigin
public class DKStoreRestController {
    private static Logger log = LogManager.getLogger();
    private final DKStoreService dkStoreService;
    private final DKUserService dkUserService;
    private final DKRoleService dkRoleService;
    private final int MAX_PERMISSION = 100;
    public DKStoreRestController(DKStoreService dkStoreService,DKUserService dkUserService,
            DKRoleService dkRoleService) {
        this.dkStoreService = dkStoreService;
        this.dkUserService = dkUserService;
        this.dkRoleService = dkRoleService;
    }

    @Transactional
    @PostMapping("/create")
    @ResponseBody
    public CreateDKStoreResponse create(@RequestBody CreateDKStoreRequest request) {

        CreateDKStoreResponse response = new CreateDKStoreResponse();
        try {
            log.info("新增门店", request);

            if(request.getName() == null || "".equals(request.getName())){
                response.setResult(ErrorNum.INVALID_PARAM_NAME);
                return response;
            }
            if(request.getAddress() == null || "".equals(request.getAddress())){
                response.setResult(ErrorNum.INVALID_PARAM_ADDRESS);
                return response;
            }
            if(request.getPhone() == null || "".equals(request.getPhone())){
                response.setResult(ErrorNum.INVALID_PARAM_PHONE);
                return response;
            }
            if(request.getToken() == null || "".equals(request.getToken())){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            if(request.getHead() == null || "".equals(request.getHead())){
                response.setResult(ErrorNum.INVALID_PARAM_HEAD);
                return response;
            }
            if(!dkUserService.tokenValid(request.getToken())){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            DKUser user = dkUserService.findUserByToken(request.getToken());
            if(user == null){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            Optional<DKRole> role = dkRoleService.findByIdAndDeleted(user.getDkRoleId() , false);
            if(role.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            if(role.get().getPermission() < MAX_PERMISSION){
                response.setResult(ErrorNum.INVALID_PARAM_PERMISSION);
                return response;
            }


            Date date = new Date();
            DKStore store = new DKStore();
            store.setCreateTime(date.getTime());
            store.setUpdateTime(date.getTime());
            store.setName(request.getName());
            store.setAddress(request.getAddress());
            store.setHead(request.getHead());
            store.setPhone(request.getPhone());
            DKStore result = dkStoreService.save(store);
            if(result != null){
                log.info("新增门店成功！");
            }else{
                response.setResult(ErrorNum.UNKNOWN);
                log.info("新增门店失败！");
            }
            return response;
        } catch (Exception e) {
            log.error("新增门店失败！");
            log.error(e);
            response.setCode(ErrorNum.UNKNOWN.getCode());
            response.setMessage(e.getMessage());
            return response;
        }

    }

    @Transactional
    @PostMapping("/modify")
    @ResponseBody
    public ModifyDKStoreResponse modify(@RequestBody ModifyDKStoreRequest request) {

        ModifyDKStoreResponse response = new ModifyDKStoreResponse();
        try {
            log.info("修改门店", request);
            if(request.getId() == null || request.getId() <= 0){
                response.setResult(ErrorNum.INVALID_PARAM_STORE_ID);
                return response;
            }
            if(request.getName() == null || "".equals(request.getName())){
                response.setResult(ErrorNum.INVALID_PARAM_NAME);
                return response;
            }
            if(request.getAddress() == null || "".equals(request.getAddress())){
                response.setResult(ErrorNum.INVALID_PARAM_ADDRESS);
                return response;
            }
            if(request.getPhone() == null || "".equals(request.getPhone())){
                response.setResult(ErrorNum.INVALID_PARAM_PHONE);
                return response;
            }
            if(request.getToken() == null || "".equals(request.getToken())){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            if(request.getHead() == null || "".equals(request.getHead())){
                response.setResult(ErrorNum.INVALID_PARAM_HEAD);
                return response;
            }
            if(!dkUserService.tokenValid(request.getToken())){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            DKUser user = dkUserService.findUserByToken(request.getToken());
            if(user == null){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            Optional<DKRole> role = dkRoleService.findByIdAndDeleted(user.getDkRoleId() , false);
            if(role.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            if(role.get().getPermission() < MAX_PERMISSION){
                response.setResult(ErrorNum.INVALID_PARAM_PERMISSION);
                return response;
            }
            Optional<DKStore> dkStoreOptional = dkStoreService.findByIdAndDeleted(request.getId() , false);
            if(!dkStoreOptional.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_STORE_ID);
                return response;
            }
            DKStore store = dkStoreOptional.get();
            Date date = new Date();
            store.setUpdateTime(date.getTime());
            store.setName(request.getName());
            store.setAddress(request.getAddress());
            store.setHead(request.getHead());
            store.setPhone(request.getPhone());
            DKStore result = dkStoreService.save(store);
            if(result != null){
                log.info("修改门店成功！");
            }else{
                response.setResult(ErrorNum.UNKNOWN);
                log.info("修改门店失败！");
            }
            return response;
        } catch (Exception e) {
            log.error("修改门店失败！");
            log.error(e);
            response.setCode(ErrorNum.UNKNOWN.getCode());
            response.setMessage(e.getMessage());
            return response;
        }

    }
}
