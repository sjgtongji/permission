package com.duofuen.permission.controller.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

public class CreateDKCatagoryResponse extends BaseResponse<CreateDKCatagoryResponse.Data>{
    public CreateDKCatagoryResponse() {
        super();
        this.data = new CreateDKCatagoryResponse.Data();
    }

    public class Data {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Integer dkCatagoryId;

        public Integer getDkCatagoryId() {
            return dkCatagoryId;
        }

        public void setDkCatagoryId(Integer dkCatagoryId) {
            this.dkCatagoryId = dkCatagoryId;
        }
    }
}
