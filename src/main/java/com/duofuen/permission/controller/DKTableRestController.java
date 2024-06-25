package com.duofuen.permission.controller;

import com.alibaba.fastjson.JSON;
import com.duofuen.permission.common.ErrorNum;
import com.duofuen.permission.controller.bean.*;
import com.duofuen.permission.domain.entity.DKStore;
import com.duofuen.permission.domain.entity.DKTable;
import com.duofuen.permission.domain.entity.DKUser;
import com.duofuen.permission.service.DKStoreService;
import com.duofuen.permission.service.DKTableService;
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
@RequestMapping(path = "/rest/dktable")
public class DKTableRestController {
    private static Logger log = LogManager.getLogger();
    private final DKStoreService dkStoreService;
    private final DKUserService dkUserService;
    private final DKTableService dkTableService;
    public DKTableRestController(DKStoreService dkStoreService,DKUserService dkUserService,
                                 DKTableService dkTableService) {
        this.dkStoreService = dkStoreService;
        this.dkUserService = dkUserService;
        this.dkTableService = dkTableService;
    }

    @Transactional
    @PostMapping("/create")
    @ResponseBody
    public CreateDKTableResponse create(@RequestBody CreateDKTableRequest request) {
        CreateDKTableResponse response = new CreateDKTableResponse();
        try {
            log.info("新增球桌", request);
            log.info(JSON.toJSONString(request));
            if(request.getToken() == null || "".equals(request.getToken())){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            if(request.getName() == null || "".equals(request.getName())){
                response.setResult(ErrorNum.INVALID_PARAM_NAME);
                return response;
            }
            if(request.getPrice() == null || request.getPrice() <= 0){
                response.setResult(ErrorNum.INVALID_PARAM_PRICE);
                return response;
            }
            if(request.getPayInterval() < 0){
                request.setPayInterval(15);
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

            Date date = new Date();
            DKTable role = new DKTable();
            role.setCreateTime(date.getTime());
            role.setUpdateTime(date.getTime());
            role.setDkStoreId(optionalDkStore.get().getId());
            role.setDkStoreName(optionalDkStore.get().getName());
            role.setDkStore(optionalDkStore.get());
            role.setName(request.getName());
            role.setPrice(request.getPrice());
            role.setPayInterval(request.getPayInterval());
            DKTable result = dkTableService.save(role);
            if(result != null){
                response.getData().setId(result.getId());
                log.info("新增球桌成功！");
            }else{
                response.setResult(ErrorNum.UNKNOWN);
                log.info("新增球桌失败！");
            }
            return response;
        }catch (Exception e){
            log.error("新增球桌失败！");
            log.error(e);
            response.setResult(ErrorNum.UNKNOWN);
            return response;
        }
    }

    @Transactional
    @PostMapping("/modify")
    @ResponseBody
    public ModifyDKTableResponse modify(@RequestBody ModifyDKTableRequest request) {
        ModifyDKTableResponse response = new ModifyDKTableResponse();
        try {
            log.info("修改球桌, {}", request);
            log.info(JSON.toJSONString(request));

            if(request.getId() == null || request.getId() <= 0){
                log.error("修改球桌失败！");
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
            if(request.getPrice() == null || request.getPrice() <= 0){
                response.setResult(ErrorNum.INVALID_PARAM_PRICE);
                return response;
            }
            if(request.getPayInterval() < 0){
                request.setPayInterval(15);
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
            Optional<DKTable> roleOptional = dkTableService.findByIdAndDeleted(request.getId() , false);
            if(!roleOptional.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_ID);
                return response;
            }
            DKTable role = roleOptional.get();

            Date date = new Date();
            role.setUpdateTime(date.getTime());
            role.setName(request.getName());
            role.setPayInterval(request.getPayInterval());
            role.setPrice(request.getPrice());
            DKTable result = dkTableService.save(role);
            if(result != null ){
                response.getData().setId(result.getId());
                log.info("修改球桌成功！");
            }else{
                response.setResult(ErrorNum.UNKNOWN);
                log.error("修改球桌失败！");
            }
            return response;
        } catch (Exception e) {
            log.error("修改球桌失败！");
            log.error(e);
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }

    @Transactional
    @GetMapping("/query")
    @ResponseBody
    public QueryDKTableResponse query(QueryDKTableRequest request) {
        QueryDKTableResponse response = new QueryDKTableResponse();
        try {
            log.info("查询球桌", request);
            log.info(JSON.toJSONString(request));

            List<DKTable> roles = new ArrayList<>();
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
            Page<DKTable> page = null;
            Pageable pageable = PageRequest.of(request.getCurrent()-1, request.getPageSize(), Sort.Direction.DESC, "createTime");
            Specification<DKTable> specification = new Specification<DKTable>() {
                @Override
                public Predicate toPredicate(Root<DKTable> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
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
            page = dkTableService.findAll(specification , pageable);

            for(DKTable item : page){
                roles.add(item);
            }
            response.getData().setData(roles);
            response.getData().setCurrent(request.getCurrent());
            response.getData().setPageSize(request.getPageSize());
            response.getData().setTotal((int) dkTableService.count(specification));
            response.getData().setSuccess(true);
            log.error("查询球桌成功！");
            return response;
        } catch (Exception e) {
            log.error("查询球桌失败！");
            log.error(e);
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }
}
