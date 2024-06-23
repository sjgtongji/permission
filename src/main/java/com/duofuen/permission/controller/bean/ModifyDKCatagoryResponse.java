package com.duofuen.permission.controller.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ModifyDKCatagoryResponse extends BaseResponse<ModifyDKCatagoryResponse.Data>  {
    public ModifyDKCatagoryResponse() {
        super();
        this.data = new ModifyDKCatagoryResponse.Data();
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
