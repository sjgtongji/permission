package com.duofuen.permission.controller.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

public class CreateRoleResponse extends  BaseResponse<CreateRoleResponse.Data>{

    public CreateRoleResponse() {
        super();
        this.data = new Data();
    }

    public class Data {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Integer roleId;

        public Integer getRoleId() {
            return roleId;
        }

        public void setRoleId(Integer roleId) {
            this.roleId = roleId;
        }
    }
}
