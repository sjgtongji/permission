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

}
