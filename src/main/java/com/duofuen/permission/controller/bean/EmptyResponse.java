package com.duofuen.permission.controller.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

public class EmptyResponse extends BaseResponse<EmptyResponse.Data>{
    public EmptyResponse() {
        super();
        this.data = new Data();
    }

    public class Data {
        private boolean success = true;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }
}
