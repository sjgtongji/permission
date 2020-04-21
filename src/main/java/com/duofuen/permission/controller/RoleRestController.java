package com.duofuen.permission.controller;

import com.alibaba.fastjson.JSON;
import com.duofuen.permission.common.ErrorNum;
import com.duofuen.permission.controller.bean.*;
import com.duofuen.permission.domain.entity.Menu;
import com.duofuen.permission.domain.entity.Project;
import com.duofuen.permission.domain.entity.Role;
import com.duofuen.permission.service.MenuService;
import com.duofuen.permission.service.ProjectService;
import com.duofuen.permission.service.RoleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.*;

@RestController
@RequestMapping(path = "/rest/role")
public class RoleRestController {
    private static Logger log = LogManager.getLogger();

    private final RoleService roleService;
    private final ProjectService projectService;
    private final MenuService menuService;
    @Autowired
    public RoleRestController(RoleService roleService, ProjectService projectService,MenuService menuService) {
        this.roleService = roleService;
        this.projectService = projectService;
        this.menuService = menuService;
    }

    @Transactional
    @PostMapping("/create")
    @ResponseBody
    public CreateRoleResponse create(@RequestBody CreateRoleRequest request) {
        CreateRoleResponse response = new CreateRoleResponse();
        try {
            log.info("创建角色, {}", request);
            log.info(JSON.toJSONString(request));
//            if(request.getAppId() == null || request.getAppId().equals("")){
//                log.error("创建角色失败！");
//                response.setResult(ErrorNum.EMPTY_APPID);
//                return response;
//            }
//
//            if(request.getAppSecret() == null || request.getAppSecret().equals("")){
//                log.error("创建角色失败！");
//                response.setResult(ErrorNum.EMTPY_APPSECRET);
//                return response;
//            }

            if(request.getName() == null || request.getName().equals("")){
                log.error("创建角色失败！");
                response.setResult(ErrorNum.INVALID_PARAM_NAME);
                return response;
            }
            Optional<Project> optionalProject = projectService.findByIdAndDeleted(request.getProjectId() , false);
            if(!optionalProject.isPresent()){
                log.error("创建角色失败！");
                response.setResult(ErrorNum.INVALID_PARAM_PJO_ID);
                return response;
            }

            List<Role> exists = roleService.findAllByCodeAndDeleted(request.getCode() , false);
            if(exists != null && exists.size() > 0){
                log.error("创建角色失败！");
                response.setResult(ErrorNum.INVALID_PARAM_ROLE_CODE_EXIST);
                return response;
            }
            Date date = new Date();
            Role role = new Role();
            role.setName(request.getName());
            role.setCode(request.getCode());
            role.setProjectId(optionalProject.get().getId());
            role.setProjectName(optionalProject.get().getName());
            role.setProject(optionalProject.get());
            role.setCreateTime(date.getTime());
            role.setUpdateTime(date.getTime());
            constructMenus(role , request.getProjectId() , request.getMenuIds());
            Role result = roleService.save(role);
            if(result != null ){
                response.getData().setRoleId(result.getId());
                log.info("创建角色成功！");
            }else{
                response.setResult(ErrorNum.UNKNOWN);
                log.error("创建角色失败！");
            }
            return response;
        } catch (Exception e) {
            log.error("创建角色失败！");
            log.error(e);
            response.setCode(ErrorNum.UNKNOWN.getCode());
            response.setMessage(e.getMessage());
            return response;
        }
    }


    @Transactional
    @PostMapping("/modify")
    @ResponseBody
    public ModifyRoleResponse modify(@RequestBody ModifyRoleRequest request) {
        ModifyRoleResponse response = new ModifyRoleResponse();
        try {
            log.info("修改角色, {}", request);
            log.info(JSON.toJSONString(request));

            if(request.getId() <= 0){
                log.error("修改角色失败！");
                response.setResult(ErrorNum.INVALID_PARAM_ROLE_ID);
                return response;
            }
            Optional<Project> optionalProject = projectService.findByIdAndDeleted(request.getProjectId() , false);
            if(!optionalProject.isPresent()){
                log.error("修改角色失败！");
                response.setResult(ErrorNum.INVALID_PARAM_PJO_ID);
                return response;
            }
            if(request.getName() == null || request.getName().equals("")){
                log.error("修改角色失败！");
                response.setResult(ErrorNum.INVALID_PARAM_NAME);
                return response;
            }
            if(request.getCode() == null || request.getCode().equals("")){
                log.error("修改角色失败！");
                response.setResult(ErrorNum.INVALID_PARAM_ROLE_CODE_EMPTY);
                return response;
            }
            List<Role> exists = roleService.findAllByCodeAndDeleted(request.getCode() , false);
            if(exists != null && exists.size() > 0){
                if(exists.size() == 1){
                    if(!exists.get(0).getId().equals(request.getId())){
                        log.error("修改角色失败！");
                        response.setResult(ErrorNum.INVALID_PARAM_ROLE_CODE_EXIST);
                        return response;
                    }
                }else{
                    log.error("修改角色失败！");
                    response.setResult(ErrorNum.INVALID_PARAM_ROLE_CODE_EXIST);
                    return response;
                }
            }

            Optional<Role> roleOptional = roleService.findByIdAndDeleted(request.getId() , false);
            if(!roleOptional.isPresent()){
                log.error("修改角色失败！");
                response.setResult(ErrorNum.INVALID_PARAM_ROLE_ID);
                return response;
            }
            Role role = roleOptional.get();
            List<Menu> menus = new ArrayList<>();
            if(request.getMenuIds() != null && request.getMenuIds().size() > 0){
                menus = menuService.findAllByIdInAndProjectIdAndDeleted(request.getMenuIds() , request.getProjectId() , false);
                if(menus.size() <= 0){
                    log.error("修改角色失败！");
                    response.setResult(ErrorNum.INVALID_PARAM_PJO_ID_OR_MENU_ID);
                    return response;
                }else if(menus.size() != request.getMenuIds().size()){
                    log.error("修改角色失败！");
                    response.setResult(ErrorNum.INVALID_PARAM_PJO_ID_OR_MENU_ID);
                    return response;
                }
            }
            Date date = new Date();
            role.setName(request.getName());
            role.setCode(request.getCode());
            role.setProjectId(optionalProject.get().getId());
            role.setProjectName(optionalProject.get().getName());
            role.setProject(optionalProject.get());
            role.setUpdateTime(date.getTime());
            role.setMenus(menus);
            role.setValid(request.isValid());
            Role result = roleService.save(role);
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
    @GetMapping("/query")
    @ResponseBody
    public QueryRoleResponse query(QueryRoleRequest request) {
        QueryRoleResponse response = new QueryRoleResponse();
        try {
            log.info("查询角色", request);
            log.info(JSON.toJSONString(request));

            List<Role> roles = new ArrayList<>();
            if(request.getCurrent() < 1 ){
                request.setCurrent(1);
            }
            if(request.getPageSize() < 0){
                request.setPageSize(20);
            }
            Page<Role> page = null;
            Pageable pageable = PageRequest.of(request.getCurrent()-1, request.getPageSize(), Sort.Direction.DESC, "createTime");
            Specification<Role> specification = new Specification<Role>() {
                @Override
                public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicates = new ArrayList<>();
                    if(request.getName() != null && !request.getName().equals("")){
                        predicates.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + request.getName() + "%"));
                    }

                    if(request.getCode() != null && !request.getCode().equals("")){
                        predicates.add(criteriaBuilder.like(root.get("code").as(String.class), "%" +request.getCode()+ "%"));
                    }

                    if(request.getProjectName() != null && !request.getProjectName().equals("")){
                        predicates.add(criteriaBuilder.like(root.get("projectName").as(String.class), "%" +request.getProjectName()+ "%"));
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
            page = roleService.findAll(specification , pageable);

            for(Role item : page){
                roles.add(item);
            }
            response.getData().setData(roles);
            response.getData().setCurrent(request.getCurrent());
            response.getData().setPageSize(request.getPageSize());
            response.getData().setTotal((int) roleService.count(specification));
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

    private boolean constructMenus(Role role , Integer projectId , List<Integer> menuIds){
        List<Menu> roots = menuService.findAllParentAndProjectIdAndDeleted(projectId , false);
        List<Menu> nodes = menuService.findAllByIdInAndProjectIdAndDeleted(menuIds , projectId, false);
        if(nodes.size() != menuIds.size()){
            return false;
        }

//        if(!makeupMissingNode(nodes , menuIds)){
//            return false;
//        }
//        List<Menu> newMenus = new ArrayList<>();
//        for(Menu root : roots){
//            if(menuIds.contains(root.getId())){
//                construct(root , menuIds);
//                newMenus.add(root);
//            }
//        }
//        log.info(JSON.toJSONString(newMenus));
        role.setMenus(nodes);
        return true;
//        List<Menu> result = new ArrayList<>();

    }

    private boolean construct(Menu root , List<Integer> menuIds){
        if(menuIds.contains(root.getId())){
            if(root.getChildren() != null && root.getChildren().size() > 0){
                List<Menu> newChildren = new ArrayList<>();
                for(Menu child : root.getChildren()){
                    construct(child  , menuIds);
                    newChildren.add(child);
                }
                root.setChildren(newChildren);
            }
            return true;
        }else{
            return false;
        }
    }
    private boolean makeupMissingNode(List<Menu> nodes, List<Integer> menuIds){
        Queue<Menu> menusQueue = new ArrayDeque<>();
        menusQueue.addAll(nodes);
        while (!menusQueue.isEmpty()) {
            // 获取并移除此队列的头，如果此队列为空，则返回 null
            Menu m = menusQueue.poll();
            if(m.getParentId() != null && !menuIds.contains(m.getParentId())){
                Optional<Menu> optionalMenu = menuService.findByIdAndDeleted(m.getParentId() , false);
                if(!optionalMenu.isPresent()){
                    return false;
                }
                nodes.add(optionalMenu.get());
                menuIds.add(optionalMenu.get().getId());
                menusQueue.add(optionalMenu.get());
            }
        }
        return true;
    }

    @Transactional
    @PostMapping("/delete")
    @ResponseBody
    public EmptyResponse delete(@RequestBody DeleteRoleRequest request) {
        EmptyResponse response = new EmptyResponse();
        try {
            log.info("删除角色", request);
            log.info(JSON.toJSONString(request));
            if(request.getId() < 0){
                response.setResult(ErrorNum.INVALID_PARAM_ROLE_ID);
                return response;
            }
            Optional<Role> roleOptional = roleService.findByIdAndDeleted(request.getId() , false);
            if(!roleOptional.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_ROLE_ID);
                return response;
            }
            Role role = roleOptional.get();
            role.setDeleted(true);
            response.setResult(roleService.save(role) != null ? ErrorNum.SUCCESS : ErrorNum.FAIL);
            log.info("删除角色成功！");
            return response;
        } catch (Exception e) {
            log.error("删除角色失败！");
            log.error(e);
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }

    @Transactional
    @PostMapping("/batchDelete")
    @ResponseBody
    public EmptyResponse batchDelete(@RequestBody BatchDeleteRoleRequest request) {
        EmptyResponse response = new EmptyResponse();
        try {
            log.info("批量删除角色", request);
            log.info(JSON.toJSONString(request));
            if(request.getIds() == null || request.getIds().length == 0){
                response.setResult(ErrorNum.INVALID_PARAM_ROLE_ID);
                return response;
            }
            List<Role> roles = roleService.findAllByIdInAndDeleted(request.getIds() , false);
            if(roles == null || roles.size() == 0){
                response.setResult(ErrorNum.INVALID_PARAM_ROLE_ID);
                return response;
            }
            for(Role role : roles){
                role.setDeleted(true);
            }
            List<Role> results = roleService.saveAll(roles);
            if(results != null && results.size() == roles.size()){
                response.setResult(ErrorNum.SUCCESS);
            }else{
                response.setResult(ErrorNum.FAIL);
            }
            log.info("批量删除角色成功！");
            return response;
        } catch (Exception e) {
            log.error("批量删除角色失败！");
            log.error(e);
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }

    @Transactional
    @PostMapping("/batchValid")
    @ResponseBody
    public EmptyResponse batchValid(@RequestBody BatchValidRoleRequest request) {
        EmptyResponse response = new EmptyResponse();
        try {
            log.info("批量启用角色", request);
            log.info(JSON.toJSONString(request));
            List<Role> roles = roleService.findAllByIdInAndDeleted(request.getIds() , false);
            if(roles == null || roles.size() == 0){
                response.setResult(ErrorNum.INVALID_PARAM_ROLE_ID);
                return response;
            }
            for(Role role : roles){
                role.setValid(true);
            }
            List<Role> results = roleService.saveAll(roles);
            if(results != null && results.size() == roles.size()){
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
    public EmptyResponse batchUnvalid(@RequestBody BatchValidRoleRequest request) {
        EmptyResponse response = new EmptyResponse();
        try {
            log.info("批量禁用角色", request);
            log.info(JSON.toJSONString(request));
            List<Role> roles = roleService.findAllByIdInAndDeleted(request.getIds(), false);
            if(roles == null || roles.size() == 0){
                response.setResult(ErrorNum.INVALID_PARAM_ROLE_ID);
                return response;
            }
            for(Role role : roles){
                role.setValid(false);
            }
            List<Role> results = roleService.saveAll(roles);
            if(results != null && results.size() == roles.size()){
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
    @GetMapping("/queryForSelect")
    @ResponseBody
    public QueryRoleForSelectResponse queryForSelect(QueryRoleForSelectRequest request) {
        QueryRoleForSelectResponse response = new QueryRoleForSelectResponse();
        try {
            log.info("查询角色", request);
            log.info(JSON.toJSONString(request));
            if(request.getProjectId() <= 0){
                log.error("查询角色失败！");
                response.setResult(ErrorNum.INVALID_PARAM_PJO_ID);
                return response;
            }
            Optional<Project> optionalProject = projectService.findByIdAndDeleted(request.getProjectId() , false);
            if(!optionalProject.isPresent()){
                log.error("修改角色失败！");
                response.setResult(ErrorNum.INVALID_PARAM_PJO_ID);
                return response;
            }
            List<Role> roles = roleService.findAllForSelect(request.getProjectId());
            response.getData().setData(roles);
            log.error("查询项目成功！");
            return response;
        } catch (Exception e) {
            log.error("查询项目失败！");
            log.error(e);
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }


}
