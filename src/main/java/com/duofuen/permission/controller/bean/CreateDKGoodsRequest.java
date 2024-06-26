package com.duofuen.permission.controller.bean;

public class CreateDKGoodsRequest {
    private String name;
    private Integer dkCatagoryId;
    private String token;
    private Integer exist;
    private Double price;
    private boolean payByCash;

    public boolean isPayByCash() {
        return payByCash;
    }

    public void setPayByCash(boolean payByCash) {
        this.payByCash = payByCash;
    }
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getExist() {
        return exist;
    }

    public void setExist(Integer exist) {
        this.exist = exist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


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
