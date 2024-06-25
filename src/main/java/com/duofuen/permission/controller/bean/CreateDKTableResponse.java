package com.duofuen.permission.controller.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

public class CreateDKTableResponse extends BaseResponse<CreateDKTableResponse.Data>{
    public CreateDKTableResponse() {
        super();
        this.data = new CreateDKTableResponse.Data();
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
