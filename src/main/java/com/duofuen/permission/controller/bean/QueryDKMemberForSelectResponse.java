package com.duofuen.permission.controller.bean;

import com.duofuen.permission.domain.entity.DKCatagory;
import com.duofuen.permission.domain.entity.DKMember;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

public class QueryDKMemberForSelectResponse extends BaseResponse<QueryDKMemberForSelectResponse.Data>{
    public QueryDKMemberForSelectResponse() {
        super();
        this.data = new QueryDKMemberForSelectResponse.Data();
    }

    public class Data {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private List<DKMember> data = new ArrayList<>();

        public List<DKMember> getData() {
            return data;
        }

        public void setData(List<DKMember> data) {
            this.data = data;
        }
    }
}
