package com.duofuen.permission.controller.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ModifyDKTableResponse extends BaseResponse<ModifyDKTableResponse.Data>  {
    public ModifyDKTableResponse() {
        super();
        this.data = new ModifyDKTableResponse.Data();
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
