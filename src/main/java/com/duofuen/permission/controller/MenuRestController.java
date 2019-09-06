package com.duofuen.permission.controller;

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
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
            CreateMenuResponse response = new CreateMenuResponse();
            if(request.getProjectId() == null){
                response.setResult(ErrorNum.INVALID_PARAM_PJO_ID);
                return response;
            }
            if(request.getName() == null || request.getName().equals("")){
                response.setResult(ErrorNum.INVALID_PARAM_NAME);
                return response;
            }
            Optional<Project> optionalProject = projectService.findById(request.getProjectId());
            if(!optionalProject.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_PJO_ID);
                return response;
            }

            if(request.getParentId() != null){
                Optional<Menu> optionalMenu = menuService.findById(request.getParentId());
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
            response.setCode(ErrorNum.UNKNOWN.getCode());
            response.setMessage(e.getMessage());
            return response;
        }
    }

    @Transactional
    @GetMapping("/query")
    @ResponseBody
    public QueryMenuResponse query(QueryMenuRequest request) {
        try {
            log.info("查询菜单", request);
            List<Menu> menus = menuService.findAllParent(PageRequest.of(request.getPage(), request.getSize(), Sort.Direction.DESC, "createTime"));
            QueryMenuResponse response = new QueryMenuResponse();
            response.getData().setList(menus);
            log.error("查询菜单成功！");
            return response;
        } catch (Exception e) {
            log.error("查询菜单失败！");
            log.error(e);
            QueryMenuResponse response = new QueryMenuResponse();
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }

}
