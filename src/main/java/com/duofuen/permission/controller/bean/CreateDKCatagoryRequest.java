package com.duofuen.permission.controller.bean;

public class CreateDKCatagoryRequest {
    private String name;
    private Integer dkStoreId;
    private String token;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDkStoreId() {
        return dkStoreId;
    }

    public void setDkStoreId(Integer dkStoreId) {
        this.dkStoreId = dkStoreId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
