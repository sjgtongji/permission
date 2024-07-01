package com.duofuen.permission.controller.bean;

import com.duofuen.permission.domain.entity.DKTableOrder;
import com.duofuen.permission.domain.entity.DKTableOrderGoods;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

public class DKTableOrderReadResponse extends BaseResponse<DKTableOrderReadResponse.Data>{
    public DKTableOrderReadResponse() {
        super();
        this.data = new DKTableOrderReadResponse.Data();
    }
    public class Data {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Integer id;
        private long startTime;
        private long endTime;
        private Double valueByCash;
        private Double valueByCard;

        private List<DKTableOrderReadResponse.Goods> goods = new ArrayList<>();

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public Double getValueByCash() {
            return valueByCash;
        }

        public void setValueByCash(Double valueByCash) {
            this.valueByCash = valueByCash;
        }

        public Double getValueByCard() {
            return valueByCard;
        }

        public void setValueByCard(Double valueByCard) {
            this.valueByCard = valueByCard;
        }

        public List<DKTableOrderReadResponse.Goods> getGoods() {
            return goods;
        }

        public void setGoods(List<DKTableOrderReadResponse.Goods> goods) {
            this.goods = goods;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public void setDKTableOrder(DKTableOrder dkTableOrder){
            if(dkTableOrder == null){
                return;
            }
            setId(dkTableOrder.getId());
            setStartTime(dkTableOrder.getStartTime());
            setEndTime(dkTableOrder.getEndTime());
            setValueByCard(dkTableOrder.getValueByCard());
            setValueByCash(dkTableOrder.getValueByCash());
        }

        public void setDKTableOrderGoods(List<DKTableOrderGoods> goodsList){
            if(goodsList == null){
                return;
            }
            List<DKTableOrderReadResponse.Goods> list = new ArrayList<>();
            for(DKTableOrderGoods goods : goodsList){
                DKTableOrderReadResponse.Goods item = new DKTableOrderReadResponse.Goods();
                item.setDKTableOrderGoods(goods);
                list.add(item);
            }
            setGoods(list);

        }
    }

    public class Goods{
        private Double price;

        private Integer quality;

        private Double totalPrice;

        private String dkGoodsName;

        private long createTime;

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public Integer getQuality() {
            return quality;
        }

        public void setQuality(Integer quality) {
            this.quality = quality;
        }

        public Double getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(Double totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getDkGoodsName() {
            return dkGoodsName;
        }

        public void setDkGoodsName(String dkGoodsName) {
            this.dkGoodsName = dkGoodsName;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public void setDKTableOrderGoods(DKTableOrderGoods dkTableOrderGoods){
            setCreateTime(dkTableOrderGoods.getCreateTime());
            setPrice(dkTableOrderGoods.getPrice());
            setQuality(dkTableOrderGoods.getQuality());
            setTotalPrice(dkTableOrderGoods.getTotalPrice());
            setDkGoodsName(dkTableOrderGoods.getDkGoodsName());
        }
    }
}
