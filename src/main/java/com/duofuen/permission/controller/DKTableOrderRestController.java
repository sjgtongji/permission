package com.duofuen.permission.controller;

import com.alibaba.fastjson.JSON;
import com.duofuen.permission.common.ErrorNum;
import com.duofuen.permission.controller.bean.*;
import com.duofuen.permission.domain.entity.*;
import com.duofuen.permission.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/rest/dktableorder")
@CrossOrigin
public class DKTableOrderRestController {
    private static Logger log = LogManager.getLogger();
    private final DKStoreService dkStoreService;
    private final DKUserService dkUserService;
    private final DKTableService dkTableService;
    private final DKTableOrderService dkTableOrderService;
    private final DKTableOrderGoodsService dkTableOrderGoodsService;
    private final DKGoodsService dkGoodsService;
    private final DKMemberService dkMemberService;
    private final DKCatagoryService dkCatagoryService;
    public DKTableOrderRestController(DKStoreService dkStoreService, DKUserService dkUserService,
                                      DKTableService dkTableService, DKTableOrderService dkTableOrderService, DKTableOrderGoodsService dkTableOrderGoodsService, DKGoodsService dkGoodsService, DKMemberService dkMemberService, DKCatagoryService dkCatagoryService) {
        this.dkStoreService = dkStoreService;
        this.dkUserService = dkUserService;
        this.dkTableService = dkTableService;
        this.dkTableOrderService = dkTableOrderService;
        this.dkTableOrderGoodsService = dkTableOrderGoodsService;
        this.dkGoodsService = dkGoodsService;
        this.dkMemberService = dkMemberService;
        this.dkCatagoryService = dkCatagoryService;
    }
    @Transactional
    @PostMapping("/start")
    @ResponseBody
    public DKTableOrderStartResponse start(@RequestBody DKTableOrderStartRequest request) {
        DKTableOrderStartResponse response = new DKTableOrderStartResponse();
        try {
            log.info("开台", request);
            log.info(JSON.toJSONString(request));
            if(request.getToken() == null || "".equals(request.getToken())){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            if(request.getDkTableId() == null || request.getDkTableId() <= 0){
                response.setResult(ErrorNum.INVALID_PARAM_TABLE_ID);
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
            Optional<DKTable> optionalDkTable = dkTableService.findByIdAndDeleted(request.getDkTableId() , false);
            if(!optionalDkTable.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_TABLE_ID);
                return response;
            }
            DKTable dkTable = optionalDkTable.get();
            if(dkTable.isOpened()){
                response.setResult(ErrorNum.INVALID_PARAM_TABLE_ID);
                return response;
            }
            DKTableOrder order = new DKTableOrder();
            Date date = new Date();
            order.setCreateTime(date.getTime());
            order.setUpdateTime(date.getTime());
            order.setDkStoreId(optionalDkStore.get().getId());
            order.setDkStoreName(optionalDkStore.get().getName());
            order.setDkStore(optionalDkStore.get());
            order.setDkTableId(dkTable.getId());
            order.setDkTable(dkTable);
            order.setDkTableName(dkTable.getName());
            order.setStartTime(date.getTime());
            order.setOpened(true);
            dkTable.setOpened(true);
            dkTable.setBooked(false);
            dkTable.setStartTime(date.getTime());
            DKTableOrder dkTableOrderResult = dkTableOrderService.save(order);
            DKTable dkTableResult = dkTableService.save(dkTable);
            if(dkTableOrderResult != null && dkTableResult != null){
                response.getData().setId(dkTableOrderResult.getId());
                log.info("开台成功！");
            }else{
                response.setResult(ErrorNum.UNKNOWN);
                log.info("开台失败！");
            }
            return response;
        }catch (Exception e){
            log.error("开台失败！");
            log.error(e);
            response.setResult(ErrorNum.UNKNOWN);
            return response;
        }
    }
    @Transactional
    @PostMapping("/buy")
    @ResponseBody
    public DKTableOrderBuyResponse buy(@RequestBody DKTableOrderBuyRequest request) {
        DKTableOrderBuyResponse response = new DKTableOrderBuyResponse();
        try {
            log.info("购买商品", request);
            log.info(JSON.toJSONString(request));
            if(request.getToken() == null || "".equals(request.getToken())){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            if(request.getDkTableId() == null || request.getDkTableId() <= 0){
                response.setResult(ErrorNum.INVALID_PARAM_TABLE_ID);
                return response;
            }
            if(request.getDkGoodsId() == null || request.getDkGoodsId() <= 0){
                response.setResult(ErrorNum.INVALID_PARAM_GOODS_ID);
                return response;
            }
            if(request.getQuality() == null || request.getQuality() <= 0){
                response.setResult(ErrorNum.INVALID_PARAM_GOODS_QUALITY);
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
            Optional<DKTable> optionalDkTable = dkTableService.findByIdAndDeleted(request.getDkTableId() , false);
            if(!optionalDkTable.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_TABLE_ID);
                return response;
            }
            DKTable dkTable = optionalDkTable.get();
            if(!dkTable.isOpened()){
                response.setResult(ErrorNum.INVALID_PARAM_TABLE_ID);
                return response;
            }
            Optional<DKGoods> dkGoodsOptional = dkGoodsService.findByIdAndDeleted(request.getDkGoodsId() , false);
            if(!dkGoodsOptional.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_GOODS_ID);
                return response;
            }

            Optional<DKCatagory> dkCatagoryOptional = dkCatagoryService.findByIdAndDeleted(dkGoodsOptional.get().getDkCatagoryId() , false);
            if(!dkCatagoryOptional.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_GOODS_ID);
                return response;
            }

            Optional<DKTableOrder> orderOptional = dkTableOrderService.findByDkStoreIdAndDkTableIdAndOpened(optionalDkStore.get().getId() , optionalDkTable.get().getId(), true);

            if(!orderOptional.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_TABLE_ID);
                return response;
            }
            DKTableOrder order = orderOptional.get();
            Date date = new Date();

            DKTableOrderGoods tableOrderGoods = new DKTableOrderGoods();
            tableOrderGoods.setCreateTime(date.getTime());
            tableOrderGoods.setUpdateTime(date.getTime());
            tableOrderGoods.setDkStoreId(optionalDkStore.get().getId());
            tableOrderGoods.setDkStoreName(optionalDkStore.get().getName());
            tableOrderGoods.setDkStore(optionalDkStore.get());
            tableOrderGoods.setDkCatagoryId(dkGoodsOptional.get().getDkCatagoryId());
            tableOrderGoods.setDkCatagoryName(dkGoodsOptional.get().getDkCatagoryName());
            tableOrderGoods.setDkCatagory(dkCatagoryOptional.get());
            tableOrderGoods.setDkGoodsId(dkGoodsOptional.get().getId());
            tableOrderGoods.setDkGoodsName(dkGoodsOptional.get().getName());
            tableOrderGoods.setDkGoods(dkGoodsOptional.get());
            tableOrderGoods.setDkTableOrderId(order.getId());
            tableOrderGoods.setDkTableOrder(order);
            tableOrderGoods.setPrice(dkGoodsOptional.get().getPrice());
            tableOrderGoods.setQuality(request.getQuality());
            tableOrderGoods.setTotalPrice(new Double(dkGoodsOptional.get().getPrice() * request.getQuality()));
            DKTableOrderGoods result = dkTableOrderGoodsService.save(tableOrderGoods);
            if(result != null){
                response.getData().setId(tableOrderGoods.getId());
            }


//            DKTableOrderGoods tableOrderGoods = new DKTableOrderGoods();
//
//            order.setCreateTime(date.getTime());
//            order.setUpdateTime(date.getTime());
//            order.setDkStoreId(optionalDkStore.get().getId());
//            order.setDkStoreName(optionalDkStore.get().getName());
//            order.setDkStore(optionalDkStore.get());
//            order.setDkTableId(dkTable.getId());
//            order.setDkTable(dkTable);
//            order.setDkTableName(dkTable.getName());
//            order.setStartTime(date.getTime());
//            dkTable.setOpened(true);
//            dkTable.setBooked(false);
//            DKTableOrder dkTableOrderResult = dkTableOrderService.save(order);
//            DKTable dkTableResult = dkTableService.save(dkTable);
//            if(dkTableOrderResult != null && dkTableResult != null){
//                response.getData().setId(dkTableOrderResult.getId());
//                log.info("开台成功！");
//            }else{
//                response.setResult(ErrorNum.UNKNOWN);
//                log.info("开台失败！");
//            }
            return response;
        }catch (Exception e){
            log.error("购买商品失败！");
            log.error(e);
            response.setResult(ErrorNum.UNKNOWN);
            return response;
        }
    }

    @Transactional
    @PostMapping("/read")
    @ResponseBody
    public DKTableOrderReadResponse read(@RequestBody DKTableOrderReadRequest request) {
        DKTableOrderReadResponse response = new DKTableOrderReadResponse();
        try {
            log.info("查看订单", request);
            log.info(JSON.toJSONString(request));
            if(request.getToken() == null || "".equals(request.getToken())){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            if(request.getDkTableId() == null || request.getDkTableId() <= 0){
                response.setResult(ErrorNum.INVALID_PARAM_TABLE_ID);
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
            Optional<DKTable> optionalDkTable = dkTableService.findByIdAndDeleted(request.getDkTableId() , false);
            if(!optionalDkTable.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_TABLE_ID);
                return response;
            }
            DKTable dkTable = optionalDkTable.get();
            if(!dkTable.isOpened()){
                response.setResult(ErrorNum.INVALID_PARAM_TABLE_ID);
                return response;
            }
            Optional<DKTableOrder> orderOptional = dkTableOrderService.findByDkStoreIdAndDkTableIdAndOpened(optionalDkStore.get().getId() , optionalDkTable.get().getId(), true);

            if(!orderOptional.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_TABLE_ID);
                return response;
            }

            DKTableOrder order = orderOptional.get();
            List<DKTableOrderGoods> dkTableOrderGoodsList = dkTableOrderGoodsService.findAllByDkStoreIdAndDkTableOrderId(optionalDkStore.get().getId() , order.getId());
            response.getData().setDKTableOrder(order);
            response.getData().setDKTableOrderGoods(dkTableOrderGoodsList);
            return response;
        }catch (Exception e){
            log.error("查看订单失败！");
            log.error(e);
            response.setResult(ErrorNum.UNKNOWN);
            return response;
        }
    }
    @Transactional
    @PostMapping("/payInfo")
    @ResponseBody
    public DKTableOrderPayInfoResponse payInfo(@RequestBody DKTableOrderPayInfoRequest request) {
        DKTableOrderPayInfoResponse response = new DKTableOrderPayInfoResponse();
        try {
            log.info("获取订单信息", request);
            log.info(JSON.toJSONString(request));
            if(request.getToken() == null || "".equals(request.getToken())){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            if(request.getDkTableId() == null || request.getDkTableId() <= 0){
                response.setResult(ErrorNum.INVALID_PARAM_TABLE_ID);
                return response;
            }
            if(request.isMember() && (request.getDkMemberId() == null || request.getDkMemberId() <= 0)){
                response.setResult(ErrorNum.INVALID_PARAM_MEMBER_ID);
                return response;
            }
            if(request.getValueForMinus() == null){
                request.setValueForMinus(new Double(0));
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

//            if(optionalRole.get().getPermission() < MAX_PERMISSION){
//                response.setResult(ErrorNum.INVALID_PARAM_PERMISSION);
//                return response;
//            }
            Optional<DKStore> optionalDkStore = dkStoreService.findByIdAndDeleted(dkUser.getDkStoreId() , false);
            if(!optionalDkStore.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_STORE_ID);
                return response;
            }
            Optional<DKTable> optionalDkTable = dkTableService.findByIdAndDeleted(request.getDkTableId() , false);
            if(!optionalDkTable.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_TABLE_ID);
                return response;
            }
            DKTable dkTable = optionalDkTable.get();
            if(!dkTable.isOpened()){
                response.setResult(ErrorNum.INVALID_PARAM_TABLE_ID);
                return response;
            }
            Optional<DKTableOrder> orderOptional = dkTableOrderService.findByDkStoreIdAndDkTableIdAndOpened(optionalDkStore.get().getId() , optionalDkTable.get().getId(), true);

            if(!orderOptional.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_TABLE_ID);
                return response;
            }

            DKTableOrder order = orderOptional.get();
            List<DKTableOrderGoods> dkTableOrderGoodsList = dkTableOrderGoodsService.findAllByDkStoreIdAndDkTableOrderId(optionalDkStore.get().getId() , order.getId());
            Date date = new Date();
            order.setEndTime(date.getTime());
            order.setMember(request.isMember());
            Double payByCash = new Double(0);
            for(DKTableOrderGoods goods : dkTableOrderGoodsList){
                payByCash += goods.getTotalPrice();
            }
            long period = order.getEndTime() - order.getStartTime();
            Double payByCard = (period / (dkTable.getPayInterval() * 1000 * 60) + 1) * dkTable.getPrice() * dkTable.getPayInterval() / 60 ;
            Double payByCardAfterMinus = payByCard - request.getValueForMinus() > 0 ? payByCard - request.getValueForMinus() : 0;
            if(request.isMember()){
                Optional<DKMember> optionalDkMember = dkMemberService.findByIdAndDeleted(request.getDkMemberId() , false);
                if(!optionalDkMember.isPresent() ){
                    response.setResult(ErrorNum.INVALID_PARAM_MEMBER_ID);
                    return response;
                }
                order.setDkMemberId(request.getDkMemberId());
                order.setDkMember(optionalDkMember.get());
                order.setDkMemberName(optionalDkMember.get().getName());
                order.setDkMemberPhone(optionalDkMember.get().getPhone());
                order.setValueByCash(payByCash);
                order.setValueByCard(payByCardAfterMinus);
                order.setOriginValue(payByCard);
                order.setValueForMinus(request.getValueForMinus());
                order.setMinusDesc(request.getMinusDesc());
                response.getData().setDkMemberValue(optionalDkMember.get().getValue());
            }else{
                order.setValueByCard(new Double(0));
                order.setValueByCash(payByCash + payByCardAfterMinus);
                order.setOriginValue(payByCash + payByCard);
                order.setValueForMinus(request.getValueForMinus());
                order.setMinusDesc(request.getMinusDesc());
            }
            if(dkTableOrderService.save(order) != null){
                response.getData().setDKTableOrder(order);
                response.getData().setDKTableOrderGoods(dkTableOrderGoodsList);
                log.info("获取订单信息成功！");
            }else{
                response.setResult(ErrorNum.UNKNOWN);
                log.info("获取订单信息失败！");
            }
            return response;

        }catch (Exception e){
            log.error("查看订单失败！");
            log.error(e);
            response.setResult(ErrorNum.UNKNOWN);
            return response;
        }
    }
    @Transactional
    @PostMapping("/end")
    @ResponseBody
    public DKTableOrderEndResponse end(@RequestBody DKTableOrderEndRequest request) {
        DKTableOrderEndResponse response = new DKTableOrderEndResponse();
        try {
            log.info("结束订单", request);
            log.info(JSON.toJSONString(request));
            if(request.getToken() == null || "".equals(request.getToken())){
                response.setResult(ErrorNum.INVALID_PARAM_TOKEN);
                return response;
            }
            if(request.getDkTableId() == null || request.getDkTableId() <= 0){
                response.setResult(ErrorNum.INVALID_PARAM_TABLE_ID);
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
            Optional<DKTable> optionalDkTable = dkTableService.findByIdAndDeleted(request.getDkTableId() , false);
            if(!optionalDkTable.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_TABLE_ID);
                return response;
            }
            DKTable dkTable = optionalDkTable.get();
            if(!dkTable.isOpened()){
                response.setResult(ErrorNum.INVALID_PARAM_TABLE_ID);
                return response;
            }
            Optional<DKTableOrder> orderOptional = dkTableOrderService.findByDkStoreIdAndDkTableIdAndOpened(optionalDkStore.get().getId() , optionalDkTable.get().getId(), true);

            if(!orderOptional.isPresent()){
                response.setResult(ErrorNum.INVALID_PARAM_TABLE_ID);
                return response;
            }

            DKTableOrder order = orderOptional.get();
            List<DKTableOrderGoods> dkTableOrderGoodsList = dkTableOrderGoodsService.findAllByDkStoreIdAndDkTableOrderId(optionalDkStore.get().getId() , order.getId());

            if(order.isMember()){
                Optional<DKMember> optionalDkMember = dkMemberService.findByIdAndDeleted(order.getDkMemberId() , false);
                if(!optionalDkMember.isPresent() ){
                    response.setResult(ErrorNum.INVALID_PARAM_MEMBER_ID);
                    return response;
                }
                if(optionalDkMember.get().getValue() < order.getValueByCard()){
                    response.setResult(ErrorNum.MEMBER_VALUE_NOT_ENOUGH);
                    return response;
                }
                DKMember dkMember = optionalDkMember.get();
                dkMember.setValue(dkMember.getValue() - order.getValueByCard());
                DKMember saveDKMemberResult = dkMemberService.save(dkMember);
                if(saveDKMemberResult == null){
                    response.setResult(ErrorNum.UNKNOWN);
                    return response;
                }
            }
            order.setOpened(false);
            dkTable.setOpened(false);
            dkTable.setBooked(false);
            DKTableOrder saveOrderResult = dkTableOrderService.save(order);
            DKTable saveTableResult = dkTableService.save(dkTable);
            if(saveOrderResult != null && saveTableResult != null){
                response.getData().setDKTableOrder(order);
                response.getData().setDKTableOrderGoods(dkTableOrderGoodsList);
            }else{
                response.setResult(ErrorNum.UNKNOWN);
            }
            return response;
        }catch (Exception e){
            log.error("结束订单失败！");
            log.error(e);
            response.setResult(ErrorNum.UNKNOWN);
            return response;
        }
    }
}
