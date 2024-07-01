package com.duofuen.permission.controller;

import com.alibaba.fastjson.JSON;
import com.duofuen.permission.common.ErrorNum;
import com.duofuen.permission.controller.bean.*;
import com.duofuen.permission.domain.entity.DKCatagory;
import com.duofuen.permission.domain.entity.DKGoods;
import com.duofuen.permission.domain.entity.DKStore;
import com.duofuen.permission.domain.entity.DKUser;
import com.duofuen.permission.service.*;
import com.duofuen.permission.service.DKGoodsService;
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
@RequestMapping(path = "/rest/dkgoods")
public class DKGoodsRestController {
    private static Logger log = LogManager.getLogger();
    private final DKStoreService dkStoreService;
    private final DKUserService dkUserService;
    private final DKRoleService dkRoleService;
    private final DKCatagoryService dkCatagoryService;
    private final DKGoodsService dkGoodsService;
    public DKGoodsRestController(DKStoreService dkStoreService,DKUserService dkUserService,
                                 DKRoleService dkRoleService, DKCatagoryService dkCatagoryService, DKGoodsService dkGoodsService) {
        this.dkStoreService = dkStoreService;
        this.dkUserService = dkUserService;
        this.dkRoleService = dkRoleService;
        this.dkCatagoryService = dkCatagoryService;
        this.dkGoodsService = dkGoodsService;
    }

    @Transactional
    @PostMapping("/create")
    @ResponseBody
    public CreateDKGoodsResponse create(@RequestBody CreateDKGoodsRequest request) {
        CreateDKGoodsResponse response = new CreateDKGoodsResponse();
        try {
            log.info("新增商品", request);
            log.info(JSON.toJSONString(request));
            if(request.getToken() == null || "".equals(request.getToken())){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            if(request.getName() == null || "".equals(request.getName())){
                response.setResult(ErrorNum.INVALID_PARAM_NAME);
                return response;
            }
            if(request.getExist() == null || request.getExist() < 0){
                response.setResult(ErrorNum.INVALID_PARAM_EXIST);
                return response;
            }
            if(request.getPrice() == null || request.getPrice() <= 0){
                response.setResult(ErrorNum.INVALID_PARAM_PRICE);
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
//            Optional<DKGoods> optionalRole = dkRoleService.findByIdAndDeleted(dkUser.getDkRoleId() , false);
//            if(!optionalRole.isPresent()){
//                response.setResult(ErrorNum.INVALID_PARAM_ROLE_ID);
//                return response;
//            }
//            if(optionalRole.get().getPermission() < MAX_PERMISSION){
//                response.setResult(ErrorNum.INVALID_PARAM_PERMISSION);
//                return response;
//            }
            Optional<DKStore> optionalDkStore = dkStoreService.findByIdAndDeleted(dkUser.getDkStoreId() , false);
            if(!optionalDkStore.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_STORE_ID);
                return response;
            }

            Optional<DKCatagory> dkCatagory = dkCatagoryService.findByIdAndDeleted(request.getDkCatagoryId() , false);

            if(!dkCatagory.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }

            Date date = new Date();
            DKGoods role = new DKGoods();
            role.setCreateTime(date.getTime());
            role.setUpdateTime(date.getTime());
            role.setDkStoreId(optionalDkStore.get().getId());
            role.setDkStoreName(optionalDkStore.get().getName());
            role.setDkStore(optionalDkStore.get());
            role.setDkCatagoryId(dkCatagory.get().getId());
            role.setDkCatagoryName(dkCatagory.get().getName());
            role.setDkCatagory(dkCatagory.get());
            role.setName(request.getName());
            role.setExist(request.getExist());
            role.setPrice(request.getPrice());
            role.setPayByCash(request.isPayByCash());
            DKGoods result = dkGoodsService.save(role);
            if(result != null){
                response.getData().setDkGoodsId(result.getId());
                log.info("新增商品成功！");
            }else{
                response.setResult(ErrorNum.UNKNOWN);
                log.info("新增商品失败！");
            }
            return response;
        }catch (Exception e){
            log.error("新增商品失败！");
            log.error(e);
            response.setResult(ErrorNum.UNKNOWN);
            return response;
        }
    }

    @Transactional
    @PostMapping("/modify")
    @ResponseBody
    public ModifyDKGoodsResponse modify(@RequestBody ModifyDKGoodsRequest request) {
        ModifyDKGoodsResponse response = new ModifyDKGoodsResponse();
        try {
            log.info("修改商品, {}", request);
            log.info(JSON.toJSONString(request));

            if(request.getId() == null || request.getId() <= 0){
                log.error("修改商品失败！");
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
            if(request.getExist() != null && request.getExist() < 0){
                response.setResult(ErrorNum.INVALID_PARAM_EXIST);
                return response;
            }
            if(request.getExist() == null || request.getExist() < 0){
                response.setResult(ErrorNum.INVALID_PARAM_EXIST);
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
            Optional<DKStore> optionalDkStore = dkStoreService.findByIdAndDeleted(dkUser.getDkStoreId() , false);
            if(!optionalDkStore.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_STORE_ID);
                return response;
            }

            Optional<DKGoods> roleOptional = dkGoodsService.findByIdAndDeleted(request.getId() , false);
            if(!roleOptional.isPresent()){
                log.error("修改商品失败！");
                response.setResult(ErrorNum.INVALID_PARAM_ROLE_ID);
                return response;
            }
            DKGoods role = roleOptional.get();

            Date date = new Date();
            role.setUpdateTime(date.getTime());
            role.setDkStoreId(optionalDkStore.get().getId());
            role.setDkStoreName(optionalDkStore.get().getName());
            role.setDkStore(optionalDkStore.get());
            role.setDkCatagoryId(role.getDkCatagoryId());
            role.setDkCatagoryName(role.getDkCatagoryName());
            role.setDkCatagory(role.getDkCatagory());
            role.setName(request.getName());
            role.setExist(request.getExist());
            role.setPrice(request.getPrice());
            role.setPayByCash(request.isPayByCash());
            DKGoods result = dkGoodsService.save(role);
            if(result != null ){
                response.getData().setId(result.getId());
                log.info("修改商品成功！");
            }else{
                response.setResult(ErrorNum.UNKNOWN);
                log.error("修改商品失败！");
            }
            return response;
        } catch (Exception e) {
            log.error("修改商品失败！");
            log.error(e);
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }

    @Transactional
    @GetMapping("/queryForSelect")
    @ResponseBody
    public QueryDKGoodsForSelectResponse queryForSelect(QueryDKGoodsForSelectRequest request) {
        QueryDKGoodsForSelectResponse response = new QueryDKGoodsForSelectResponse();
        try {
            log.info("查询商品", request);
            log.info(JSON.toJSONString(request));
            DKUser dkUser = dkUserService.findUserByToken(request.getToken());
            if(dkUser == null){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            Optional<DKStore> optionalProject = dkStoreService.findByIdAndDeleted(dkUser.getDkStoreId() , false);
            if(!optionalProject.isPresent()){
                log.error("查询商品失败！");
                response.setResult(ErrorNum.INVALID_PARAM_STORE_ID);
                return response;
            }
            Integer dkStoreId = dkUser.getDkStoreId();
            List<DKGoods> roles = dkGoodsService.findAllByDkStoreIdAndDkCatagoryIdAndDeleted(dkStoreId , request.getDkCatagoryId(), false);
            response.getData().setData(roles);
            log.error("查询商品成功！");
            return response;
        } catch (Exception e) {
            log.error("查询商品失败！");
            log.error(e);
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }

    @Transactional
    @GetMapping("/query")
    @ResponseBody
    public QueryDKGoodsResponse query(QueryDKGoodsRequest request) {
        QueryDKGoodsResponse response = new QueryDKGoodsResponse();
        try {
            log.info("查询商品", request);
            log.info(JSON.toJSONString(request));

            List<DKGoods> roles = new ArrayList<>();
            if(request.getCurrent() < 1 ){
                request.setCurrent(1);
            }
            if(request.getPageSize() < 0){
                request.setPageSize(20);
            }
            DKUser dkUser = dkUserService.findUserByToken(request.getToken());
            if(dkUser == null){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            Optional<DKStore> dkStore = dkStoreService.findByIdAndDeleted(dkUser.getDkStoreId() , false);
            if(!dkStore.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            Page<DKGoods> page = null;
            Pageable pageable = PageRequest.of(request.getCurrent()-1, request.getPageSize(), Sort.Direction.DESC, "createTime");
            Specification<DKGoods> specification = new Specification<DKGoods>() {
                @Override
                public Predicate toPredicate(Root<DKGoods> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicates = new ArrayList<>();
                    if(request.getName() != null && !request.getName().equals("")){
                        predicates.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + request.getName() + "%"));
                    }

//                    if(request.getValid() != null && !request.getValid().equals("")){
//                        if(request.getValid().equals("true")){
//                            predicates.add(criteriaBuilder.isTrue(root.get("valid")));
//                        }else if(request.getValid().equals("false")){
//                            predicates.add(criteriaBuilder.isFalse(root.get("valid")));
//                        }
//                    }
                    predicates.add(criteriaBuilder.isFalse(root.get("deleted")));
                    predicates.add(criteriaBuilder.equal(root.get("dkStoreId") , dkUser.getDkStoreId()));
                    Predicate[] p = new Predicate[predicates.size()];
                    return criteriaBuilder.and(predicates.toArray(p));
                }
            };
            page = dkGoodsService.findAll(specification , pageable);

            for(DKGoods item : page){
                roles.add(item);
            }
            response.getData().setData(roles);
            response.getData().setCurrent(request.getCurrent());
            response.getData().setPageSize(request.getPageSize());
            response.getData().setTotal((int) dkGoodsService.count(specification));
            response.getData().setSuccess(true);
            log.error("查询商品成功！");
            return response;
        } catch (Exception e) {
            log.error("查询商品失败！");
            log.error(e);
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }
}

