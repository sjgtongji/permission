package com.duofuen.permission.controller.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

public class LoginResponse extends BaseResponse<LoginResponse.Data>{
    public LoginResponse() {
        super();
        this.data = new Data();
    }

    public class Data {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String deviceId;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Integer deviceNo;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String token;

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public Integer getDeviceNo() {
            return deviceNo;
        }

        public void setDeviceNo(Integer deviceNo) {
            this.deviceNo = deviceNo;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
