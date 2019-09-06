package com.duofuen.permission.controller.bean;

import com.duofuen.permission.domain.entity.Project;
import com.duofuen.permission.domain.entity.Role;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

public class QueryRoleResponse extends BaseResponse<QueryRoleResponse.Data>{
    public QueryRoleResponse() {
        super();
        this.data = new Data();
    }

    public class Data {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private List<Role> list = new ArrayList<>();

        public List<Role> getList() {
            return list;
        }

        public void setList(List<Role> list) {
            this.list = list;
        }
    }
}
