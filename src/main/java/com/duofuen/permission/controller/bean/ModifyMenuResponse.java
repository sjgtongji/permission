package com.duofuen.permission.controller.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ModifyMenuResponse extends BaseResponse<ModifyMenuResponse.Data>{
    public ModifyMenuResponse() {
        super();
        this.data = new ModifyMenuResponse.Data();
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
