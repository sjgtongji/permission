package com.duofuen.permission.controller.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ModifyDKMenuResponse extends BaseResponse<ModifyDKMenuResponse.Data> {
    public ModifyDKMenuResponse() {
        super();
        this.data = new ModifyDKMenuResponse.Data();
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
