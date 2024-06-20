package com.duofuen.permission.controller;

import com.alibaba.fastjson.JSON;
import com.duofuen.permission.common.ErrorNum;
import com.duofuen.permission.controller.bean.*;
import com.duofuen.permission.domain.entity.DKMenu;
import com.duofuen.permission.domain.entity.DKRole;
import com.duofuen.permission.domain.entity.DKStore;
import com.duofuen.permission.domain.entity.DKUser;
import com.duofuen.permission.service.DKMenuService;
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
public class DKMenuRestController {
    private static Logger log = LogManager.getLogger();
    private final DKStoreService dkStoreService;
    private final DKUserService dkUserService;
    private final DKRoleService dkRoleService;
    private final DKMenuService dkMenuService;
    private final int MAX_PERMISSION = 100;
    public DKMenuRestController(DKStoreService dkStoreService,DKUserService dkUserService,
                                 DKRoleService dkRoleService, DKMenuService dkMenuService) {
        this.dkStoreService = dkStoreService;
        this.dkUserService = dkUserService;
        this.dkRoleService = dkRoleService;
        this.dkMenuService = dkMenuService;
    }

    @Transactional
    @PostMapping("/create")
    @ResponseBody
    public CreateDKMenuResponse create(@RequestBody CreateDKMenuRequest request) {
        CreateDKMenuResponse response = new CreateDKMenuResponse();
        try{
            if(request.getName() == null || "".equals(request.getName())){
                response.setResult(ErrorNum.INVALID_PARAM_NAME);
                return response;
            }
            if(request.getPath() == null || "".equals(request.getPath())){
                response.setResult(ErrorNum.INVALID_PARAM_PATH);
                return response;
            }
            if(request.getToken() == null || "".equals(request.getToken())){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            if(request.getStoreId() == null){
                response.setResult(ErrorNum.INVALID_PARAM_STORE_ID);
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
            Optional<DKStore> dkStore = dkStoreService.findByIdAndDeleted(request.getStoreId() , false);
            if(!dkStore.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_STORE_ID);
                return response;
            }
            Date date = new Date();
            DKMenu dkMenu = new DKMenu();
            dkMenu.setCreateTime(date.getTime());
            dkMenu.setUpdateTime(date.getTime());
            dkMenu.setName(request.getName());
            dkMenu.setPath(request.getPath());
            dkMenu.setPermission(request.getPermission());
            dkMenu.setDkStoreId(dkStore.get().getId());
            dkMenu.setDkStoreName(dkStore.get().getName());
            DKMenu result = dkMenuService.save(dkMenu);
            if(result != null){
                log.info("新增菜单成功！");
            }else{
                response.setResult(ErrorNum.UNKNOWN);
                log.info("新增菜单失败！");
            }
            return response;
        }catch (Exception e){
            log.error("新增菜单失败！");
            log.error(e);
            response.setResult(ErrorNum.UNKNOWN);
            return response;
        }

    }

    @Transactional
    @PostMapping("/modify")
    @ResponseBody
    public ModifyDKMenuResponse modify(@RequestBody ModifyDKMenuRequest request) {
        ModifyDKMenuResponse response = new ModifyDKMenuResponse();
        try {
            log.info("修改菜单", request);
            log.info(JSON.toJSONString(request));
            if(request.getId() == null || request.getId() < 0){
                response.setResult(ErrorNum.INVALID_PARAM_MENU_ID);
                return response;
            }
            if(request.getName() == null || "".equals(request.getName())){
                response.setResult(ErrorNum.INVALID_PARAM_NAME);
                return response;
            }
            if(request.getPath() == null || "".equals(request.getPath())){
                response.setResult(ErrorNum.INVALID_PARAM_PATH);
                return response;
            }
            if(request.getToken() == null || "".equals(request.getToken())){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            if(request.getStoreId() == null){
                response.setResult(ErrorNum.INVALID_PARAM_STORE_ID);
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
            Optional<DKStore> dkStore = dkStoreService.findByIdAndDeleted(request.getStoreId() , false);
            if(!dkStore.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_STORE_ID);
                return response;
            }
            Optional<DKMenu> menuOptional = dkMenuService.findByIdAndDeleted(request.getId() , false);
            if(!menuOptional.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_MENU_ID);
                return response;
            }
            DKMenu menu = menuOptional.get();
            Date date = new Date();
            menu.setCreateTime(date.getTime());
            menu.setUpdateTime(date.getTime());
            menu.setName(request.getName());
            menu.setPath(request.getPath());
            menu.setPermission(request.getPermission());
            menu.setDkStoreId(dkStore.get().getId());
            menu.setDkStoreName(dkStore.get().getName());
            DKMenu result = dkMenuService.save(menu);
            if(result != null){
                response.getData().setId(result.getId());
                log.info("修改菜单成功！");
            }else{
                response.setResult(ErrorNum.UNKNOWN);
                log.info("修改菜单失败！");
            }
            return response;
        } catch (Exception e) {
            log.error("修改菜单失败！");
            log.error(e);
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }
}
