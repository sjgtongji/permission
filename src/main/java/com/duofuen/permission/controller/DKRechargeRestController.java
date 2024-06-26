package com.duofuen.permission.controller;

import com.alibaba.fastjson.JSON;
import com.duofuen.permission.common.ErrorNum;
import com.duofuen.permission.controller.bean.*;
import com.duofuen.permission.domain.entity.DKMember;
import com.duofuen.permission.domain.entity.DKRecharge;
import com.duofuen.permission.domain.entity.DKStore;
import com.duofuen.permission.domain.entity.DKUser;
import com.duofuen.permission.service.DKMemberService;
import com.duofuen.permission.service.DKRechargeService;
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
@RequestMapping(path = "/rest/dkrecharge")
public class DKRechargeRestController {
    private static Logger log = LogManager.getLogger();
    private final DKStoreService dkStoreService;
    private final DKUserService dkUserService;
    private final DKMemberService dkMemberService;
    private final DKRechargeService dkRechargeService;
    public DKRechargeRestController(DKStoreService dkStoreService,DKUserService dkUserService,
                                  DKMemberService dkMemberService, DKRechargeService dkRechargeService) {
        this.dkStoreService = dkStoreService;
        this.dkUserService = dkUserService;
        this.dkMemberService = dkMemberService;
        this.dkRechargeService = dkRechargeService;
    }

    @Transactional
    @PostMapping("/create")
    @ResponseBody
    public CreateDKRechargeResponse create(@RequestBody CreateDKRechargeRequest request) {
        CreateDKRechargeResponse response = new CreateDKRechargeResponse();
        try {
            log.info("新增会员充值", request);
            log.info(JSON.toJSONString(request));
            if(request.getToken() == null || "".equals(request.getToken())){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            if(request.getRechargeValue() == null || request.getRechargeValue() < 0){
                response.setResult(ErrorNum.INVALID_PARAM_RECHARGE_VALUE);
                return response;
            }
            if(request.getExtraValue() == null || request.getExtraValue() < 0){
                response.setResult(ErrorNum.INVALID_PARAM_EXTRA_VALUE);
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

            Optional<DKMember> memberOptional = dkMemberService.findByIdAndDeleted(request.getDkMemberId() , false);
            if(!memberOptional.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_MEMBER_ID);
                return response;
            }
            DKMember member = memberOptional.get();


            Date date = new Date();
            DKRecharge role = new DKRecharge();
            Double valueAfterRecharge = new Double(memberOptional.get().getValue().doubleValue() + request.getRechargeValue().doubleValue() + request.getExtraValue().doubleValue());
            role.setCreateTime(date.getTime());
            role.setUpdateTime(date.getTime());
            role.setDkStoreId(optionalDkStore.get().getId());
            role.setDkStoreName(optionalDkStore.get().getName());
            role.setDkStore(optionalDkStore.get());
            role.setDkMemberId(memberOptional.get().getId());
            role.setDkMember(memberOptional.get());
            role.setDkMemberName(memberOptional.get().getName());
            role.setDkMemberPhone(member.getPhone());
            role.setDkUserId(dkUser.getId());
            role.setDkUser(dkUser);
            role.setDkUserName(dkUser.getUserName());
            role.setRechargeValue(request.getRechargeValue());
            role.setExtraValue(request.getExtraValue());
            role.setTotalValue(new Double(request.getRechargeValue().doubleValue() + request.getExtraValue().doubleValue()));
            role.setValueBeforeRecharge(memberOptional.get().getValue());
            role.setValueAfterRecharge(valueAfterRecharge);


            DKRecharge result = dkRechargeService.save(role);
            if(result != null){

                member.setValue(valueAfterRecharge);
                member.setSum(new Double(member.getSum().doubleValue() + request.getRechargeValue().doubleValue()));
                member.setScore(new Double(member.getScore().doubleValue() + request.getRechargeValue().doubleValue()));
                DKMember saveMemberResult = dkMemberService.save(member);
                if(saveMemberResult != null){
                    response.getData().setId(result.getId());
                }else{
                    response.setResult(ErrorNum.UNKNOWN);
                }
                log.info("新增会员充值成功！");
            }else{
                response.setResult(ErrorNum.UNKNOWN);
                log.info("新增会员充值失败！");
            }
            return response;
        }catch (Exception e){
            log.error("新增会员充值失败！");
            log.error(e);
            response.setResult(ErrorNum.UNKNOWN);
            return response;
        }
    }

    @Transactional
    @GetMapping("/query")
    @ResponseBody
    public QueryDKRechargeResponse query(QueryDKRechargeRequest request) {
        QueryDKRechargeResponse response = new QueryDKRechargeResponse();
        try {
            log.info("查询会员充值", request);
            log.info(JSON.toJSONString(request));
            if(request.getToken() == null || "".equals(request.getToken())){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            List<DKRecharge> roles = new ArrayList<>();
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
            Page<DKRecharge> page = null;
            Pageable pageable = PageRequest.of(request.getCurrent()-1, request.getPageSize(), Sort.Direction.DESC, "createTime");
            Specification<DKRecharge> specification = new Specification<DKRecharge>() {
                @Override
                public Predicate toPredicate(Root<DKRecharge> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicates = new ArrayList<>();
                    if(request.getDkMemberName() != null && !request.getDkMemberName().equals("")){
                        predicates.add(criteriaBuilder.like(root.get("dkMemberName").as(String.class), "%" + request.getDkMemberName() + "%"));
                    }

                    if(request.getDkMemberPhone() != null && !request.getDkMemberPhone().equals("")){
                        predicates.add(criteriaBuilder.like(root.get("dkMemberPhone").as(String.class), "%" +request.getDkMemberPhone()+ "%"));
                    }
                    if(request.getDkUserName() != null && !request.getDkUserName().equals("")){
                        predicates.add(criteriaBuilder.like(root.get("dkUserName").as(String.class), "%" +request.getDkUserName()+ "%"));
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
            page = dkRechargeService.findAll(specification , pageable);

            for(DKRecharge item : page){
                roles.add(item);
            }
            response.getData().setData(roles);
            response.getData().setCurrent(request.getCurrent());
            response.getData().setPageSize(request.getPageSize());
            response.getData().setTotal((int) dkRechargeService.count(specification));
            response.getData().setSuccess(true);
            log.error("查询会员充值成功！");
            return response;
        } catch (Exception e) {
            log.error("查询会员充值失败！");
            log.error(e);
            response.setResult(ErrorNum.FAIL);
            return response;
        }
    }
}
