package com.duofuen.permission.controller.bean;

public class BaseResponse<T> {

    protected Integer errorCode;
    protected String errorMessage;
    protected T data;

    protected BaseResponse() {
        errorCode = 0;
        errorMessage = "";
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
