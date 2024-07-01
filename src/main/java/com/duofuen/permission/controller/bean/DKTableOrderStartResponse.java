package com.duofuen.permission.controller.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

public class DKTableOrderStartResponse extends BaseResponse<DKTableOrderStartResponse.Data>{
    public DKTableOrderStartResponse() {
        super();
        this.data = new DKTableOrderStartResponse.Data();
    }
    public class Data {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Integer id;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }
}
