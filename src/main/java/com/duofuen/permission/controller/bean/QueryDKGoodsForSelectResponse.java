package com.duofuen.permission.controller.bean;

import com.duofuen.permission.domain.entity.DKCatagory;
import com.duofuen.permission.domain.entity.DKGoods;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

public class QueryDKGoodsForSelectResponse extends BaseResponse<QueryDKGoodsForSelectResponse.Data>{
    public QueryDKGoodsForSelectResponse() {
        super();
        this.data = new QueryDKGoodsForSelectResponse.Data();
    }

    public class Data {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private List<DKGoods> data = new ArrayList<>();

        public List<DKGoods> getData() {
            return data;
        }

        public void setData(List<DKGoods> data) {
            this.data = data;
        }
    }

}
