package com.duofuen.permission.controller.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

public class CreateUserResponse extends BaseResponse<CreateUserResponse.Data> {
    public CreateUserResponse() {
        super();
        this.data = new Data();
    }

    public class Data {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Integer userId;

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }
    }
}
