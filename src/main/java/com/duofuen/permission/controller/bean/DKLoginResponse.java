package com.duofuen.permission.controller.bean;

import com.fasterxml.jackson.annotation.JsonInclude;


public class DKLoginResponse extends BaseResponse<DKLoginResponse.Data>{
    public DKLoginResponse() {
        super();
        this.data = new Data();
    }

    public class Data {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Integer userId;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String token;

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}