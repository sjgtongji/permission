package com.duofuen.permission.controller;

import com.alibaba.fastjson.JSON;
import com.duofuen.permission.common.ErrorNum;
import com.duofuen.permission.controller.bean.*;
import com.duofuen.permission.domain.entity.DKCatagory;
import com.duofuen.permission.domain.entity.DKStore;
import com.duofuen.permission.domain.entity.DKUser;
import com.duofuen.permission.service.DKCatagoryService;
import com.duofuen.permission.service.DKCatagoryService;
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
@RequestMapping(path = "/rest/dkcatagory")
public class DKCatagoryRestController {
    private static Logger log = LogManager.getLogger();
    private final DKStoreService dkStoreService;
    private final DKUserService dkUserService;
    private final DKCatagoryService dkRoleService;
    private final DKCatagoryService dkCatagoryService;
    public DKCatagoryRestController(DKStoreService dkStoreService,DKUserService dkUserService,
                                DKCatagoryService dkRoleService, DKCatagoryService dkCatagoryService) {
        this.dkStoreService = dkStoreService;
        this.dkUserService = dkUserService;
        this.dkRoleService = dkRoleService;
        this.dkCatagoryService = dkCatagoryService;
    }

    @Transactional
    @PostMapping("/create")
    @ResponseBody
    public CreateDKCatagoryResponse create(@RequestBody CreateDKCatagoryRequest request) {
        CreateDKCatagoryResponse response = new CreateDKCatagoryResponse();
        try {
            log.info("新增商品类别", request);
            log.info(JSON.toJSONString(request));
            if(request.getToken() == null || "".equals(request.getToken())){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            if(request.getName() == null || "".equals(request.getName())){
                response.setResult(ErrorNum.INVALID_PARAM_NAME);
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
//            Optional<DKCatagory> optionalRole = dkRoleService.findByIdAndDeleted(dkUser.getDkRoleId() , false);
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
            DKCatagory role = new DKCatagory();
            role.setCreateTime(date.getTime());
            role.setUpdateTime(date.getTime());
            role.setDkStoreId(optionalDkStore.get().getId());
            role.setDkStoreName(optionalDkStore.get().getName());
            role.setDkStore(optionalDkStore.get());
            role.setName(request.getName());
            DKCatagory result = dkRoleService.save(role);
            if(result != null){
                response.getData().setDkCatagoryId(result.getId());
                log.info("新增商品类别成功！");
            }else{
                response.setResult(ErrorNum.UNKNOWN);
                log.info("新增商品类别失败！");
            }
            return response;
        }catch (Exception e){
            log.error("新增商品类别失败！");
            log.error(e);
            response.setResult(ErrorNum.UNKNOWN);
            return response;
        }
    }

    @Transactional
    @PostMapping("/modify")
    @ResponseBody
    public ModifyDKCatagoryResponse modify(@RequestBody ModifyDKCatagoryRequest request) {
        ModifyDKCatagoryResponse response = new ModifyDKCatagoryResponse();
        try {
            log.info("修改商品类别, {}", request);
            log.info(JSON.toJSONString(request));

            if(request.getId() == null || request.getId() <= 0){
                log.error("修改商品类别失败！");
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
            Optional<DKCatagory> optionalRole = dkRoleService.findByIdAndDeleted(dkUser.getDkRoleId() , false);
            if(!optionalRole.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_ROLE_ID);
                return response;
            }
            Optional<DKStore> optionalDkStore = dkStoreService.findByIdAndDeleted(request.getDkStoreId() , false);
            if(!optionalDkStore.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_STORE_ID);
                return response;
            }

            Optional<DKCatagory> roleOptional = dkRoleService.findByIdAndDeleted(request.getId() , false);
            if(!roleOptional.isPresent()){
                log.error("修改商品类别失败！");
                response.setResult(ErrorNum.INVALID_PARAM_ROLE_ID);
                return response;
            }
            DKCatagory role = roleOptional.get();

            Date date = new Date();
            role.setUpdateTime(date.getTime());
            role.setDkStoreId(optionalDkStore.get().getId());
            role.setDkStoreName(optionalDkStore.get().getName());
            role.setDkStore(optionalDkStore.get());
            role.setName(request.getName());
            DKCatagory result = dkRoleService.save(role);
            if(result != null ){
                response.getData().setId(result.getId());
                log.info("修改商品类别成功！");
            }else{
                response.setResult(ErrorNum.UNKNOWN);
                log.error("修改商品类别失败！");
            }
            return response;
        } catch (Exception e) {
            log.error("修改商品类别失败！");
            log.error(e);
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }

    @Transactional
    @GetMapping("/queryForSelect")
    @ResponseBody
    public QueryDKCatagoryForSelectResponse queryForSelect(QueryDKCatagoryForSelectRequest request) {
        QueryDKCatagoryForSelectResponse response = new QueryDKCatagoryForSelectResponse();
        try {
            log.info("查询商品类别", request);
            log.info(JSON.toJSONString(request));
            if(request.getDkStoreId() == null || request.getDkStoreId() <= 0){
                log.error("查询商品类别失败！");
                response.setResult(ErrorNum.INVALID_PARAM_STORE_ID);
                return response;
            }
            Optional<DKStore> optionalProject = dkStoreService.findByIdAndDeleted(request.getDkStoreId() , false);
            if(!optionalProject.isPresent()){
                log.error("查询商品类别失败！");
                response.setResult(ErrorNum.INVALID_PARAM_STORE_ID);
                return response;
            }
            List<DKCatagory> roles = dkCatagoryService.findAllByDkStoreId(request.getDkStoreId());
            response.getData().setData(roles);
            log.error("查询商品类别成功！");
            return response;
        } catch (Exception e) {
            log.error("查询商品类别失败！");
            log.error(e);
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }

    @Transactional
    @GetMapping("/query")
    @ResponseBody
    public QueryDKCatagoryResponse query(QueryDKCatagoryRequest request) {
        QueryDKCatagoryResponse response = new QueryDKCatagoryResponse();
        try {
            log.info("查询商品类别", request);
            log.info(JSON.toJSONString(request));

            List<DKCatagory> roles = new ArrayList<>();
            if(request.getCurrent() < 1 ){
                request.setCurrent(1);
            }
            if(request.getPageSize() < 0){
                request.setPageSize(20);
            }
            Page<DKCatagory> page = null;
            Pageable pageable = PageRequest.of(request.getCurrent()-1, request.getPageSize(), Sort.Direction.DESC, "createTime");
            Specification<DKCatagory> specification = new Specification<DKCatagory>() {
                @Override
                public Predicate toPredicate(Root<DKCatagory> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicates = new ArrayList<>();
                    if(request.getName() != null && !request.getName().equals("")){
                        predicates.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + request.getName() + "%"));
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
            page = dkCatagoryService.findAll(specification , pageable);

            for(DKCatagory item : page){
                roles.add(item);
            }
            response.getData().setData(roles);
            response.getData().setCurrent(request.getCurrent());
            response.getData().setPageSize(request.getPageSize());
            response.getData().setTotal((int) dkRoleService.count(specification));
            response.getData().setSuccess(true);
            log.error("查询商品类别成功！");
            return response;
        } catch (Exception e) {
            log.error("查询商品类别失败！");
            log.error(e);
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }
}
