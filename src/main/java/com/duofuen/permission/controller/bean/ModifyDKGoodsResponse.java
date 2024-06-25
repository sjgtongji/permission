package com.duofuen.permission.controller.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ModifyDKGoodsResponse extends BaseResponse<ModifyDKGoodsResponse.Data> {
    public ModifyDKGoodsResponse() {
        super();
        this.data = new ModifyDKGoodsResponse.Data();
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
