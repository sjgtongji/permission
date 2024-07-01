package com.duofuen.permission.controller.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

public class DKTableOrderBuyResponse extends BaseResponse<DKTableOrderBuyResponse.Data>{
    public DKTableOrderBuyResponse() {
        super();
        this.data = new DKTableOrderBuyResponse.Data();
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
