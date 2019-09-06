package com.duofuen.permission.controller.bean;

import com.duofuen.permission.domain.entity.Project;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

public class QueryProjectResponse extends BaseResponse<QueryProjectResponse.Data>{
    public QueryProjectResponse(){
        super();
        this.data = new Data();
    }
    public class Data {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private List<Project> list = new ArrayList<>();

        public List<Project> getList() {
            return list;
        }

        public void setList(List<Project> list) {
            this.list = list;
        }
    }
}
