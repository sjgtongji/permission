package com.duofuen.permission.controller.bean;

import com.duofuen.permission.domain.entity.Project;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

public class QueryProjectForSelectResponse extends BaseResponse<QueryProjectForSelectResponse.Data>{
    public QueryProjectForSelectResponse() {
        super();
        this.data = new QueryProjectForSelectResponse.Data();
    }

    public class Data {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private List<Project> data = new ArrayList<>();

        public List<Project> getData() {
            return data;
        }

        public void setData(List<Project> data) {
            this.data = data;
        }
    }
}
