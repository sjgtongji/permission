package com.duofuen.permission.controller;

import com.alibaba.fastjson.JSON;
import com.duofuen.permission.common.ErrorNum;
import com.duofuen.permission.controller.bean.*;
import com.duofuen.permission.domain.entity.DKCatagory;
import com.duofuen.permission.domain.entity.DKMember;
import com.duofuen.permission.domain.entity.DKStore;
import com.duofuen.permission.domain.entity.DKUser;
import com.duofuen.permission.service.DKCatagoryService;
import com.duofuen.permission.service.DKMemberService;
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
@RequestMapping(path = "/rest/dkmember")
public class DKMemberRestController {
    private static Logger log = LogManager.getLogger();
    private final DKStoreService dkStoreService;
    private final DKUserService dkUserService;
    private final DKMemberService dkMemberService;
    public DKMemberRestController(DKStoreService dkStoreService,DKUserService dkUserService,
                                    DKMemberService dkMemberService) {
        this.dkStoreService = dkStoreService;
        this.dkUserService = dkUserService;
        this.dkMemberService = dkMemberService;
    }

    @Transactional
    @PostMapping("/create")
    @ResponseBody
    public CreateDKMemberResponse create(@RequestBody CreateDKMemberRequest request) {
        CreateDKMemberResponse response = new CreateDKMemberResponse();
        try {
            log.info("新增会员", request);
            log.info(JSON.toJSONString(request));
            if(request.getToken() == null || "".equals(request.getToken())){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            if(request.getName() == null || "".equals(request.getName())){
                response.setResult(ErrorNum.INVALID_PARAM_NAME);
                return response;
            }
            if(request.getPhone() == null || "".equals(request.getPhone())){
                response.setResult(ErrorNum.INVALID_PARAM_PHONE);
                return response;
            }
            if(request.getPhone().length() != 11){
                response.setResult(ErrorNum.INVALID_PARAM_PHONE_LENGTH);
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
//            Optional<DKMember> optionalRole = dkRoleService.findByIdAndDeleted(dkUser.getDkRoleId() , false);
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

            Optional<DKMember> member = dkMemberService.findByDkStoreIdAndPhoneAndDeleted(dkUser.getDkStoreId() , request.getPhone() , false);
            if(member.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_PHONE_EXIST);
                return response;
            }

            Date date = new Date();
            DKMember role = new DKMember();
            role.setCreateTime(date.getTime());
            role.setUpdateTime(date.getTime());
            role.setDkStoreId(optionalDkStore.get().getId());
            role.setDkStoreName(optionalDkStore.get().getName());
            role.setDkStore(optionalDkStore.get());
            role.setName(request.getName());
            role.setPhone(request.getPhone());
            role.setValue(new Double(0));
            role.setScore(new Double(0));
            role.setSum(new Double(0));
            DKMember result = dkMemberService.save(role);
            if(result != null){
                response.getData().setDkMemberId(result.getId());
                log.info("新增会员成功！");
            }else{
                response.setResult(ErrorNum.UNKNOWN);
                log.info("新增会员失败！");
            }
            return response;
        }catch (Exception e){
            log.error("新增会员失败！");
            log.error(e);
            response.setResult(ErrorNum.UNKNOWN);
            return response;
        }
    }

    @Transactional
    @PostMapping("/modify")
    @ResponseBody
    public ModifyDKMemberResponse modify(@RequestBody ModifyDKMemberRequest request) {
        ModifyDKMemberResponse response = new ModifyDKMemberResponse();
        try {
            log.info("修改会员, {}", request);
            log.info(JSON.toJSONString(request));

            if(request.getId() == null || request.getId() <= 0){
                log.error("修改会员失败！");
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
            if(!dkUserService.tokenValid(request.getToken())){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            DKUser dkUser = dkUserService.findUserByToken(request.getToken());
            if(dkUser == null){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            Optional<DKMember> roleOptional = dkMemberService.findByIdAndDeleted(request.getId() , false);
            if(!roleOptional.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_ID);
                return response;
            }
            DKMember role = roleOptional.get();

            Date date = new Date();
            role.setUpdateTime(date.getTime());
            role.setName(request.getName());
            DKMember result = dkMemberService.save(role);
            if(result != null ){
                response.getData().setId(result.getId());
                log.info("修改会员成功！");
            }else{
                response.setResult(ErrorNum.UNKNOWN);
                log.error("修改会员失败！");
            }
            return response;
        } catch (Exception e) {
            log.error("修改会员失败！");
            log.error(e);
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }

    @Transactional
    @GetMapping("/queryForSelect")
    @ResponseBody
    public QueryDKMemberForSelectResponse queryForSelect(QueryDKMemberForSelectRequest request) {
        QueryDKMemberForSelectResponse response = new QueryDKMemberForSelectResponse();
        try {
            log.info("查询会员", request);
            log.info(JSON.toJSONString(request));
            DKUser dkUser = dkUserService.findUserByToken(request.getToken());
            if(dkUser == null){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            Optional<DKStore> optionalProject = dkStoreService.findByIdAndDeleted(dkUser.getDkStoreId() , false);
            if(!optionalProject.isPresent()){
                log.error("查询会员失败！");
                response.setResult(ErrorNum.INVALID_PARAM_STORE_ID);
                return response;
            }
            List<DKMember> roles = dkMemberService.findAllByDkStoreId(dkUser.getDkStoreId());
            response.getData().setData(roles);
            log.error("查询会员成功！");
            return response;
        } catch (Exception e) {
            log.error("查询会员失败！");
            log.error(e);
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }

    @Transactional
    @GetMapping("/query")
    @ResponseBody
    public QueryDKMemberResponse query(QueryDKMemberRequest request) {
        QueryDKMemberResponse response = new QueryDKMemberResponse();
        try {
            log.info("查询会员", request);
            log.info(JSON.toJSONString(request));

            List<DKMember> roles = new ArrayList<>();
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
            Page<DKMember> page = null;
            Pageable pageable = PageRequest.of(request.getCurrent()-1, request.getPageSize(), Sort.Direction.DESC, "createTime");
            Specification<DKMember> specification = new Specification<DKMember>() {
                @Override
                public Predicate toPredicate(Root<DKMember> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicates = new ArrayList<>();
                    if(request.getName() != null && !request.getName().equals("")){
                        predicates.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + request.getName() + "%"));
                    }

                    if(request.getPhone() != null && !request.getPhone().equals("")){
                        predicates.add(criteriaBuilder.like(root.get("phone").as(String.class), "%" +request.getPhone()+ "%"));
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
            page = dkMemberService.findAll(specification , pageable);

            for(DKMember item : page){
                roles.add(item);
            }
            response.getData().setData(roles);
            response.getData().setCurrent(request.getCurrent());
            response.getData().setPageSize(request.getPageSize());
            response.getData().setTotal((int) dkMemberService.count(specification));
            response.getData().setSuccess(true);
            log.error("查询会员成功！");
            return response;
        } catch (Exception e) {
            log.error("查询会员失败！");
            log.error(e);
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }
}
