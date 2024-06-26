package com.duofuen.permission.controller.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

public class CreateDKRechargeResponse extends BaseResponse<CreateDKRechargeResponse.Data>{
    public CreateDKRechargeResponse() {
        super();
        this.data = new CreateDKRechargeResponse.Data();
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
