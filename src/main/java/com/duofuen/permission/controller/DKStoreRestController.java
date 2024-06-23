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
            log.info(JSON.toJSONString(request));

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
                log.info("token过期", request);
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            if(!dkUserService.isAdmin(request.getToken())){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            DKUser user = dkUserService.findUserByToken(request.getToken());
            if(user == null){
                log.info("没有用户", request);
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
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
                response.getData().setId(result.getId());
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
            if(!dkUserService.isAdmin(request.getToken())){
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

    @Transactional
    @GetMapping("/query")
    @ResponseBody
    public QueryDKStoreResponse query(QueryDKStoreRequest request) {
        QueryDKStoreResponse response = new QueryDKStoreResponse();
        try {
            log.info("查询门店", request);
            log.info(JSON.toJSONString(request));

            List<DKStore> stores = new ArrayList<>();
            if(request.getCurrent() < 1 ){
                request.setCurrent(1);
            }
            if(request.getPageSize() < 0){
                request.setPageSize(20);
            }
            Page<DKStore> page = null;
            Pageable pageable = PageRequest.of(request.getCurrent()-1, request.getPageSize(), Sort.Direction.DESC, "createTime");
            Specification<DKStore> specification = new Specification<DKStore>() {
                @Override
                public Predicate toPredicate(Root<DKStore> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicates = new ArrayList<>();
                    if(request.getName() != null && !request.getName().equals("")){
                        predicates.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + request.getName() + "%"));
                    }

                    if(request.getAddress() != null && !request.getAddress().equals("")){
                        predicates.add(criteriaBuilder.like(root.get("address").as(String.class), "%" +request.getAddress()+ "%"));
                    }

                    if(request.getPhone() != null && !request.getPhone().equals("")){
                        predicates.add(criteriaBuilder.like(root.get("phone").as(String.class), "%" +request.getPhone()+ "%"));
                    }

                    if(request.getHead() != null && !request.getHead().equals("")){
                        predicates.add(criteriaBuilder.like(root.get("head").as(String.class), "%" +request.getHead()+ "%"));
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
            page = dkStoreService.findAll(specification , pageable);

            for(DKStore item : page){
                stores.add(item);
            }
            response.getData().setData(stores);
            response.getData().setCurrent(request.getCurrent());
            response.getData().setPageSize(request.getPageSize());
            response.getData().setTotal((int) dkStoreService.count(specification));
            response.getData().setSuccess(true);
            log.error("查询门店成功！");
            return response;
        } catch (Exception e) {
            log.error("查询门店失败！");
            log.error(e);
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }

    @Transactional
    @GetMapping("/queryForSelect")
    @ResponseBody
    public QueryDKStoreForSelectResponse queryForSelect(QueryDKStoreForSelectRequest request) {
        QueryDKStoreForSelectResponse response = new QueryDKStoreForSelectResponse();
        try {
            log.info("查询门店", request);
            log.info(JSON.toJSONString(request));
            List<DKStore> stores = dkStoreService.findAllForSelect();
            response.getData().setData(stores);
            log.error("查询门店成功！");
            return response;
        } catch (Exception e) {
            log.error("查询门店失败！");
            log.error(e);
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }
}
