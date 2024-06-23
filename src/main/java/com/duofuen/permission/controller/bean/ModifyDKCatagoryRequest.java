package com.duofuen.permission.controller.bean;

public class ModifyDKCatagoryRequest {
    private Integer id;
    private String name;
    private Integer dkStoreId;
    private String token;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
