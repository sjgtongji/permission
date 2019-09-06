package com.duofuen.permission.controller.bean;

import com.duofuen.permission.common.ErrorNum;



public class BaseResponse<T> {

    protected Integer code;
    protected String message;
    protected T data;

    protected BaseResponse() {
        setResult(ErrorNum.SUCCESS);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setResult(ErrorNum error){
        this.code = error.getCode();
        this.message = error.getMsg();
    }

}
