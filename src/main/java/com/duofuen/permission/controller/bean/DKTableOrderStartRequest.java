package com.duofuen.permission.controller.bean;

public class DKTableOrderStartRequest {
    private String token;
    private Integer dkTableId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getDkTableId() {
        return dkTableId;
    }

    public void setDkTableId(Integer dkTableId) {
        this.dkTableId = dkTableId;
    }
}
