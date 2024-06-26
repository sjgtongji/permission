package com.duofuen.permission.controller.bean;

public class CreateDKRechargeRequest {
    private String token;
    private Integer dkMemberId;
    private Double rechargeValue;
    private Double extraValue;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getDkMemberId() {
        return dkMemberId;
    }

    public void setDkMemberId(Integer dkMemberId) {
        this.dkMemberId = dkMemberId;
    }

    public Double getRechargeValue() {
        return rechargeValue;
    }

    public void setRechargeValue(Double rechargeValue) {
        this.rechargeValue = rechargeValue;
    }

    public Double getExtraValue() {
        return extraValue;
    }

    public void setExtraValue(Double extraValue) {
        this.extraValue = extraValue;
    }
}
