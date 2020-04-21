package com.duofuen.permission.controller.bean;

import com.duofuen.permission.domain.entity.Project;
import com.duofuen.permission.domain.entity.Role;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

public class QueryRoleForSelectResponse extends BaseResponse<QueryRoleForSelectResponse.Data>{
    public QueryRoleForSelectResponse() {
        super();
        this.data = new QueryRoleForSelectResponse.Data();
    }

    public class Data {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private List<Role> data = new ArrayList<>();

        public List<Role> getData() {
            return data;
        }

        public void setData(List<Role> data) {
            this.data = data;
        }
    }
}
