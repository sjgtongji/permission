package com.duofuen.permission.controller.bean;

public class QueryDKCatagoryRequest {
    private int current = 1;
    private int pageSize = 20;
    private String createTime;
    private String name = "";
    private String sorter = "";
    private String dkStoreName;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSorter() {
        return sorter;
    }

    public void setSorter(String sorter) {
        this.sorter = sorter;
    }

    public String getDkStoreName() {
        return dkStoreName;
    }

    public void setDkStoreName(String dkStoreName) {
        this.dkStoreName = dkStoreName;
    }
}
