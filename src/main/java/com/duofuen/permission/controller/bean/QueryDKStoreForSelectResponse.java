package com.duofuen.permission.controller.bean;

import com.duofuen.permission.domain.entity.DKStore;
import com.duofuen.permission.domain.entity.Project;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

public class QueryDKStoreForSelectResponse extends BaseResponse<QueryDKStoreForSelectResponse.Data>{
    public QueryDKStoreForSelectResponse() {
        super();
        this.data = new QueryDKStoreForSelectResponse.Data();
    }

    public class Data {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private List<DKStore> data = new ArrayList<>();

        public List<DKStore> getData() {
            return data;
        }

        public void setData(List<DKStore> data) {
            this.data = data;
        }
    }
}
