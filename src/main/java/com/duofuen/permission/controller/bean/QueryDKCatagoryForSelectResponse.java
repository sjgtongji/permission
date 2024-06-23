package com.duofuen.permission.controller.bean;

import com.duofuen.permission.domain.entity.DKCatagory;
import com.duofuen.permission.domain.entity.DKRole;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

public class QueryDKCatagoryForSelectResponse extends BaseResponse<QueryDKCatagoryForSelectResponse.Data>{
    public QueryDKCatagoryForSelectResponse() {
        super();
        this.data = new QueryDKCatagoryForSelectResponse.Data();
    }

    public class Data {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private List<DKCatagory> data = new ArrayList<>();

        public List<DKCatagory> getData() {
            return data;
        }

        public void setData(List<DKCatagory> data) {
            this.data = data;
        }
    }
}
