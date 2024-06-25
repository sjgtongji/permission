package com.duofuen.permission.controller.bean;

public class QueryDKGoodsForSelectRequest {
    private String token;
    private Integer dkCatagoryId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getDkCatagoryId() {
        return dkCatagoryId;
    }

    public void setDkCatagoryId(Integer dkCatagoryId) {
        this.dkCatagoryId = dkCatagoryId;
    }
}
