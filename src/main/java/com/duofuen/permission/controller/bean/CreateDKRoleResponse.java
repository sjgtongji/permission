package com.duofuen.permission.controller.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

public class CreateDKRoleResponse extends  BaseResponse<CreateDKRoleResponse.Data> {
    public CreateDKRoleResponse() {
        super();
        this.data = new CreateDKRoleResponse.Data();
    }

    public class Data {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Integer dkRoleId;

        public Integer getDkRoleId() {
            return dkRoleId;
        }

        public void setDkRoleId(Integer dkRoleId) {
            this.dkRoleId = dkRoleId;
        }
    }
}
