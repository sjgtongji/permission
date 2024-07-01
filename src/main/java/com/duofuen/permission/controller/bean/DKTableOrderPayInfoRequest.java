package com.duofuen.permission.controller.bean;

public class DKTableOrderPayInfoRequest {
    private String token;
    private Integer dkTableId;
    private boolean member;
    private Integer dkMemberId;
    private String minusDesc;
    private Double valueForMinus;

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

    public boolean isMember() {
        return member;
    }

    public void setMember(boolean member) {
        this.member = member;
    }

    public Integer getDkMemberId() {
        return dkMemberId;
    }

    public void setDkMemberId(Integer dkMemberId) {
        this.dkMemberId = dkMemberId;
    }

    public String getMinusDesc() {
        return minusDesc;
    }

    public void setMinusDesc(String minusDesc) {
        this.minusDesc = minusDesc;
    }

    public Double getValueForMinus() {
        return valueForMinus;
    }

    public void setValueForMinus(Double valueForMinus) {
        this.valueForMinus = valueForMinus;
    }
}
