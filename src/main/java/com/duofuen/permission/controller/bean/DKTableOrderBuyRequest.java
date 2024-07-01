package com.duofuen.permission.controller.bean;

public class DKTableOrderBuyRequest {
    private String token;
    private Integer dkTableId;
    private Integer dkGoodsId;
    private Integer quality;

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

    public Integer getDkGoodsId() {
        return dkGoodsId;
    }

    public void setDkGoodsId(Integer dkGoodsId) {
        this.dkGoodsId = dkGoodsId;
    }

    public Integer getQuality() {
        return quality;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }
}
