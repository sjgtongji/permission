package com.duofuen.permission.controller.bean;

public class ModifyDKTableRequest {
    private Integer id;
    private String name;
    private String token;
    private long payInterval = 15;
    private Double price;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getPayInterval() {
        return payInterval;
    }

    public void setPayInterval(long payInterval) {
        this.payInterval = payInterval;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
