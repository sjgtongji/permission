package com.duofuen.permission.controller;

import com.alibaba.fastjson.JSON;
import com.duofuen.permission.common.ErrorNum;
import com.duofuen.permission.controller.bean.*;
import com.duofuen.permission.domain.entity.Menu;
import com.duofuen.permission.domain.entity.Project;
import com.duofuen.permission.domain.entity.Role;
import com.duofuen.permission.domain.entity.User;
import com.duofuen.permission.service.MenuService;
import com.duofuen.permission.service.ProjectService;
import com.duofuen.permission.service.RoleService;
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
@RequestMapping(path = "/rest/menu")
public class MenuRestController {
    private static Logger log = LogManager.getLogger();
    private final MenuService menuService;
    private final ProjectService projectService;
    private final RoleService roleService;

    public MenuRestController(MenuService menuService, ProjectService projectService, RoleService roleService) {
        this.menuService = menuService;
        this.projectService = projectService;
        this.roleService = roleService;
    }

    @Transactional
    @PostMapping("/create")
    @ResponseBody
    public CreateMenuResponse create(@RequestBody CreateMenuRequest request) {
        try {
            log.info("新增菜单", request);
            log.info(JSON.toJSONString(request));
            CreateMenuResponse response = new CreateMenuResponse();
            if(request.getProjectId() == null || request.getProjectId().equals("")){
                response.setResult(ErrorNum.INVALID_PARAM_PJO_ID);
                return response;
            }
            if(request.getName() == null || request.getName().equals("")){
                response.setResult(ErrorNum.INVALID_PARAM_NAME);
                return response;
            }
            if(request.getUrl() == null || request.getUrl().equals("")){
                response.setResult(ErrorNum.INVALID_PARAM_URL);
                return response;
            }
            if(request.getComponent() == null || request.getComponent().equals("")){
                response.setResult(ErrorNum.INVALID_PARAM_COMP);
                return response;
            }

            Optional<Project> optionalProject = projectService.findByIdAndDeleted(request.getProjectId(), false);
            if(!optionalProject.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_PJO_ID);
                return response;
            }

            if(request.getParentId() != null){
                Optional<Menu> optionalMenu = menuService.findByIdAndDeleted(request.getParentId(),false);
                if(!optionalMenu.isPresent() || optionalMenu.get().getProjectId() == null || !optionalMenu.get().getProjectId().equals(request.getProjectId())){
                    response.setResult(ErrorNum.INVALID_PARAM_PARENT_ID);
                    return response;
                }
            }

            Date date = new Date();
            Menu menu = new Menu();
            menu.setCreateTime(date.getTime());
            menu.setUpdateTime(date.getTime());
            menu.setProjectId(request.getProjectId());
            menu.setProjectName(optionalProject.get().getName());
            menu.setComponent(request.getComponent());
            menu.setName(request.getName());
            menu.setUrl(request.getUrl());
            menu.setParentId(request.getParentId());

            Menu result = menuService.save(menu);
            if(result != null){
                response.getData().setMenuId(result.getId());
                log.info("新增菜单成功！");
            }else{
                response.setResult(ErrorNum.UNKNOWN);
                log.info("新增菜单失败！");
            }
            return response;
        } catch (Exception e) {
            log.error("新增菜单失败！");
            log.error(e);
            CreateMenuResponse response = new CreateMenuResponse();
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }

    @Transactional
    @GetMapping("/query")
    @ResponseBody
    public QueryMenuResponse query(QueryMenuRequest request) {
        QueryMenuResponse response = new QueryMenuResponse();
        try {
            log.info("查询菜单", request);
            if(request.getCurrent() < 1 ){
                request.setCurrent(1);
            }
            if(request.getPageSize() < 0){
                request.setPageSize(20);
            }
            List<Menu> result = new ArrayList<>();
            Page<Menu> page = null;
            Pageable pageable = PageRequest.of(request.getCurrent()-1, request.getPageSize(), Sort.Direction.DESC, "createTime");
            Specification<Menu> specification = new Specification<Menu>() {
                @Override
                public Predicate toPredicate(Root<Menu> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicates = new ArrayList<>();
                    if(request.getName() != null && !request.getName().equals("")){
                        predicates.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + request.getName() + "%"));
                    }

                    if(request.getUrl() != null && !request.getUrl().equals("")){
                        predicates.add(criteriaBuilder.like(root.get("url").as(String.class), "%" +request.getUrl()+ "%"));
                    }

                    if(request.getProjectName() != null && !request.getProjectName().equals("")){
                        predicates.add(criteriaBuilder.like(root.get("projectName").as(String.class), "%" +request.getProjectName()+ "%"));
                    }

                    predicates.add(criteriaBuilder.isFalse(root.get("deleted")));
                    predicates.add(criteriaBuilder.isNull(root.get("parentId")));
                    Predicate[] p = new Predicate[predicates.size()];
                    return criteriaBuilder.and(predicates.toArray(p));
                }
            };
            page = menuService.findAll(specification , pageable);

            for(Menu item : page){
                result.add(item);
            }
            response.getData().setData(result);
            response.getData().setCurrent(request.getCurrent());
            response.getData().setPageSize(request.getPageSize());
            response.getData().setTotal((int) menuService.count(specification));
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
    @PostMapping("/modify")
    @ResponseBody
    public ModifyMenuResponse modify(@RequestBody ModifyMenuRequest request) {
        ModifyMenuResponse response = new ModifyMenuResponse();
        try {
            log.info("修改菜单", request);
            log.info(JSON.toJSONString(request));
            if(request.getProjectId() == null || request.getProjectId().equals("")){
                response.setResult(ErrorNum.INVALID_PARAM_PJO_ID);
                return response;
            }
            if(request.getId() == null || request.getId() < 0){
                response.setResult(ErrorNum.INVALID_PARAM_MENU_ID);
                return response;
            }
            if(request.getName() == null || request.getName().equals("")){
                response.setResult(ErrorNum.INVALID_PARAM_NAME);
                return response;
            }
            if(request.getUrl() == null || request.getUrl().equals("")){
                response.setResult(ErrorNum.INVALID_PARAM_URL);
                return response;
            }
            if(request.getComponent() == null || request.getComponent().equals("")){
                response.setResult(ErrorNum.INVALID_PARAM_COMP);
                return response;
            }
            Optional<Menu> menuOptional = menuService.findByIdAndDeleted(request.getId() , false);
            if(!menuOptional.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_MENU_ID);
                return response;
            }
            Optional<Project> optionalProject = projectService.findByIdAndDeleted(request.getProjectId(), false);
            if(!optionalProject.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_PJO_ID);
                return response;
            }

            if(request.getParentId() != null){
                Optional<Menu> optionalMenu = menuService.findByIdAndDeleted(request.getParentId(), false);
                if(!optionalMenu.isPresent() || optionalMenu.get().getProjectId() == null || !optionalMenu.get().getProjectId().equals(request.getProjectId())){
                    response.setResult(ErrorNum.INVALID_PARAM_PARENT_ID);
                    return response;
                }
            }

            Date date = new Date();
            Menu menu = menuOptional.get();
            menu.setUpdateTime(date.getTime());
            menu.setProjectId(request.getProjectId());
            menu.setProjectName(optionalProject.get().getName());
            menu.setComponent(request.getComponent());
            menu.setName(request.getName());
            menu.setUrl(request.getUrl());
            menu.setParentId(request.getParentId());
            menu.setValid(request.isValid());

            Menu result = menuService.save(menu);
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
    @PostMapping("/delete")
    @ResponseBody
    public EmptyResponse delete(@RequestBody DeleteMenuRequest request) {
        EmptyResponse response = new EmptyResponse();
        try {
            log.info("删除菜单", request);
            log.info(JSON.toJSONString(request));
            if(request.getId() < 0){
                response.setResult(ErrorNum.INVALID_PARAM_MENU_ID);
                return response;
            }

            Optional<Menu> menuOptional = menuService.findByIdAndDeleted(request.getId() , false);
            if(!menuOptional.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_MENU_ID);
                return response;
            }
            List<Menu> children = new ArrayList<>();
            menuService.findAllChildren(menuOptional.get() , children);

            menuOptional.get().setDeleted(true);
            for(Menu child : children){
                child.setDeleted(true);
            }
            children.add(menuOptional.get());
            List<Menu> result = menuService.saveAll(children);
            if(result != null && result.size() == children.size()){
                response.getData().setSuccess(true);
                log.info("删除菜单成功！");
            }else{
                response.setResult(ErrorNum.UNKNOWN);
                log.info("删除菜单失败！");
            }
            return response;
        } catch (Exception e) {
            log.error("删除菜单失败！");
            log.error(e);
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }

    @Transactional
    @GetMapping("/queryForSelect")
    @ResponseBody
    public QueryMenuForSelectResponse queryForSelect(QueryMenuForSelectRequest request) {
        QueryMenuForSelectResponse response = new QueryMenuForSelectResponse();
        try {
            log.info("请求菜单数据", request);
            log.info(JSON.toJSONString(request));
            if(request.getProjectId() <= 0){
                response.setResult(ErrorNum.INVALID_PARAM_PJO_ID);
                return response;
            }


            List<Menu> menus = menuService.findAllParentAndProjectIdAndDeleted(request.getProjectId() , false);
            response.constructResponseMenu(menus);

            if(request.getRoleId() > 0){
                Optional<Role> roleOptional = roleService.findByIdAndDeleted(request.getRoleId() , false);
                if(roleOptional.isPresent()){
                    Role role = roleOptional.get();
                    response.constructResponseCheckKeys(role.getMenus());
                }
            }
            return response;
        } catch (Exception e) {
            log.error("请求菜单数据失败！");
            log.error(e);
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }

    @Transactional
    @GetMapping("/adminPanel")
    @ResponseBody
    public AdminPanelMenuResponse query(AdminPanelMenuRequest request) {
        log.info("请求后台菜单数据", request);
        log.info(JSON.toJSONString(request));
        AdminPanelMenuResponse response = new AdminPanelMenuResponse();
        try {
            List<AdminPanelMenuResponse.AdminPanelMenu> menus = new ArrayList<>();
            AdminPanelMenuResponse.AdminPanelMenu menu1 = new AdminPanelMenuResponse.AdminPanelMenu("/projects", "项目管理" ,"");
            AdminPanelMenuResponse.AdminPanelMenu menu2 = new AdminPanelMenuResponse.AdminPanelMenu("/menus", "菜单管理" ,"");
            AdminPanelMenuResponse.AdminPanelMenu menu3 = new AdminPanelMenuResponse.AdminPanelMenu("/roles", "角色管理" ,"");
            AdminPanelMenuResponse.AdminPanelMenu menu4 = new AdminPanelMenuResponse.AdminPanelMenu("/users", "用户管理" ,"");
            AdminPanelMenuResponse.AdminPanelMenu menu5 = new AdminPanelMenuResponse.AdminPanelMenu("/stores", "门店管理" ,"");
            AdminPanelMenuResponse.AdminPanelMenu menu6 = new AdminPanelMenuResponse.AdminPanelMenu("/dkroles", "角色管理" ,"");
            AdminPanelMenuResponse.AdminPanelMenu menu7 = new AdminPanelMenuResponse.AdminPanelMenu("/dkusers", "用户管理" ,"");
            AdminPanelMenuResponse.AdminPanelMenu menu8 = new AdminPanelMenuResponse.AdminPanelMenu("/dkmenus", "菜单管理" ,"");
            menus.add(menu1);
            menus.add(menu2);
            menus.add(menu3);
            menus.add(menu4);
            menus.add(menu5);
            menus.add(menu6);
            menus.add(menu7);
            menus.add(menu8);
            response.setResult(ErrorNum.SUCCESS);
            response.getData().setMenus(menus);
            return response;
        }catch (Exception e){
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }
}
