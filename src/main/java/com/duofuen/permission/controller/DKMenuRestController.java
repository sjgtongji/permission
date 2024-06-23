package com.duofuen.permission.controller;

import com.alibaba.fastjson.JSON;
import com.duofuen.permission.common.ErrorNum;
import com.duofuen.permission.controller.bean.*;
import com.duofuen.permission.domain.entity.*;
import com.duofuen.permission.service.DKMenuService;
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
        log.info("新增菜单", request);
        log.info(JSON.toJSONString(request));
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
            if(request.getDkStoreId() == null || request.getDkStoreId() <= 0){
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
            if(!dkUserService.isAdmin(request.getToken())){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            Optional<DKRole> role = dkRoleService.findByIdAndDeleted(user.getDkRoleId() , false);
            if(!role.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            Optional<DKStore> dkStore = dkStoreService.findByIdAndDeleted(request.getDkStoreId() , false);
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
            if(request.getDkStoreId() == null || request.getDkStoreId() <= 0){
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
            if(!dkUserService.isAdmin(request.getToken())){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            Optional<DKRole> role = dkRoleService.findByIdAndDeleted(user.getDkRoleId() , false);
            if(!role.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            Optional<DKStore> dkStore = dkStoreService.findByIdAndDeleted(request.getDkStoreId() , false);
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

    @Transactional
    @GetMapping("/query")
    @ResponseBody
    public QueryDKMenuResponse query(QueryDKMenuRequest request) {
        QueryDKMenuResponse response = new QueryDKMenuResponse();
        try {
            log.info("查询菜单", request);
            if(request.getCurrent() < 1 ){
                request.setCurrent(1);
            }
            if(request.getPageSize() < 0){
                request.setPageSize(20);
            }
            List<DKMenu> result = new ArrayList<>();
            Page<DKMenu> page = null;
            Pageable pageable = PageRequest.of(request.getCurrent()-1, request.getPageSize(), Sort.Direction.DESC, "createTime");
            Specification<DKMenu> specification = new Specification<DKMenu>() {
                @Override
                public Predicate toPredicate(Root<DKMenu> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicates = new ArrayList<>();
                    if(request.getName() != null && !request.getName().equals("")){
                        predicates.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + request.getName() + "%"));
                    }

                    if(request.getPath() != null && !request.getPath().equals("")){
                        predicates.add(criteriaBuilder.like(root.get("path").as(String.class), "%" +request.getPath()+ "%"));
                    }

                    if(request.getDkStoreName() != null && !request.getDkStoreName().equals("")){
                        predicates.add(criteriaBuilder.like(root.get("dkStoreName").as(String.class), "%" +request.getDkStoreName()+ "%"));
                    }

                    predicates.add(criteriaBuilder.isFalse(root.get("deleted")));
                    Predicate[] p = new Predicate[predicates.size()];
                    return criteriaBuilder.and(predicates.toArray(p));
                }
            };
            page = dkMenuService.findAll(specification , pageable);

            for(DKMenu item : page){
                result.add(item);
            }
            response.getData().setData(result);
            response.getData().setCurrent(request.getCurrent());
            response.getData().setPageSize(request.getPageSize());
            response.getData().setTotal((int) dkMenuService.count(specification));
            response.getData().setSuccess(true);
            log.error("查询菜单成功！");
            return response;
        } catch (Exception e) {
            log.error("查询菜单失败！");
            log.error(e);
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }

    @Transactional
    @PostMapping("/adminPanel")
    @ResponseBody
    public AdminPanelMenuResponse query(@RequestBody AdminPanelMenuRequest request) {
        log.info("请求后台菜单数据", request);
        log.info(JSON.toJSONString(request));
        AdminPanelMenuResponse response = new AdminPanelMenuResponse();
        try {
            List<AdminPanelMenuResponse.AdminPanelMenu> menus = new ArrayList<>();
            if(request.getToken() == null || "".equals(request.getToken())){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
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
            if(dkUserService.isAdmin(request.getToken())){
                AdminPanelMenuResponse.AdminPanelMenu menu1 = new AdminPanelMenuResponse.AdminPanelMenu("/stores", "门店管理" ,"");
                AdminPanelMenuResponse.AdminPanelMenu menu2 = new AdminPanelMenuResponse.AdminPanelMenu("/dkroles", "角色管理" ,"");
                AdminPanelMenuResponse.AdminPanelMenu menu3 = new AdminPanelMenuResponse.AdminPanelMenu("/dkusers", "用户管理" ,"");
                AdminPanelMenuResponse.AdminPanelMenu menu4 = new AdminPanelMenuResponse.AdminPanelMenu("/dkmenus", "菜单管理" ,"");
                menus.add(menu1);
                menus.add(menu2);
                menus.add(menu3);
                menus.add(menu4);
            }
            Optional<DKRole> role = dkRoleService.findByIdAndDeleted(user.getDkRoleId() , false);
            if(!role.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            if(!role.get().getDkStoreId().equals(user.getDkStoreId())){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            Optional<DKStore> dkStore = dkStoreService.findByIdAndDeleted(user.getDkStoreId() , false);
            if(!dkStore.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_STORE_ID);
                return response;
            }

            List<DKMenu> dkMenus = dkMenuService.findAllByDkStoreId(user.getDkStoreId());
            for(DKMenu dkMenu : dkMenus){
                if(dkMenu.getPermission() <= role.get().getPermission()){
                    AdminPanelMenuResponse.AdminPanelMenu menu = new AdminPanelMenuResponse.AdminPanelMenu(dkMenu.getPath(), dkMenu.getName() ,"");
                    menus.add(menu);
                }
            }
            response.setResult(ErrorNum.SUCCESS);
            response.getData().setMenus(menus);
            return response;
        }catch (Exception e){
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }
}
