package com.duofuen.permission.controller.bean;

public class CreateDKUserRequest {
    private String token;
    private Integer dkRoleId;
    private String userName;
    private String phone;
    private String email;
    private Integer dkStoreId;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getDkRoleId() {
        return dkRoleId;
    }

    public void setDkRoleId(Integer dkRoleId) {
        this.dkRoleId = dkRoleId;
    }

    public Integer getDkStoreId() {
        return dkStoreId;
    }

    public void setDkStoreId(Integer dkStoreId) {
        this.dkStoreId = dkStoreId;
    }
}
