package com.duofuen.permission.controller.bean;

public class CreateDKTableRequest {
    private String name;
    private String token;
    private long payInterval = 15;
    private Double price;

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

}
