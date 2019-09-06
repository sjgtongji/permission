package com.duofuen.permission.controller.bean;

import com.duofuen.permission.domain.entity.Menu;
import com.duofuen.permission.domain.entity.Project;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

public class QueryMenuResponse extends BaseResponse<QueryMenuResponse.Data> {
    public QueryMenuResponse() {
        super();
        this.data = new Data();
    }
    public class Data {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private List<Menu> list = new ArrayList<>();

        public List<Menu> getList() {
            return list;
        }

        public void setList(List<Menu> list) {
            this.list = list;
        }
    }
}
