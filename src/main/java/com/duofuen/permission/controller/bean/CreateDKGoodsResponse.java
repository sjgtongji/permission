package com.duofuen.permission.controller.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

public class CreateDKGoodsResponse extends BaseResponse<CreateDKGoodsResponse.Data>{
    public CreateDKGoodsResponse() {
        super();
        this.data = new CreateDKGoodsResponse.Data();
    }

    public class Data {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Integer dkGoodsId;

        public Integer getDkGoodsId() {
            return dkGoodsId;
        }

        public void setDkGoodsId(Integer dkGoodsId) {
            this.dkGoodsId = dkGoodsId;
        }
    }
}
