package com.duofuen.permission.controller.bean;

import com.duofuen.permission.domain.entity.DKRole;
import com.duofuen.permission.domain.entity.DKStore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

public class QueryDKRoleForSelectResponse extends BaseResponse<QueryDKRoleForSelectResponse.Data>{
    public QueryDKRoleForSelectResponse() {
        super();
        this.data = new QueryDKRoleForSelectResponse.Data();
    }

    public class Data {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private List<DKRole> data = new ArrayList<>();

        public List<DKRole> getData() {
            return data;
        }

        public void setData(List<DKRole> data) {
            this.data = data;
        }
    }
}
