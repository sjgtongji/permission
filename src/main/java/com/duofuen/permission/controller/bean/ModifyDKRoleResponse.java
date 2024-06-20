package com.duofuen.permission.controller.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ModifyDKRoleResponse extends BaseResponse<ModifyDKRoleResponse.Data> {
    public ModifyDKRoleResponse() {
        super();
        this.data = new ModifyDKRoleResponse.Data();
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
