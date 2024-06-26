package com.duofuen.permission.controller.bean;

public class QueryDKRechargeRequest {

    private int current = 1;
    private int pageSize = 20;
    private String createTime;
    private String sorter = "";
    private String token;
    private String dkMemberPhone;
    private String dkUserName;
    private String dkMemberName;

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

    public String getSorter() {
        return sorter;
    }

    public void setSorter(String sorter) {
        this.sorter = sorter;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDkMemberPhone() {
        return dkMemberPhone;
    }

    public void setDkMemberPhone(String dkMemberPhone) {
        this.dkMemberPhone = dkMemberPhone;
    }

    public String getDkUserName() {
        return dkUserName;
    }

    public void setDkUserName(String dkUserName) {
        this.dkUserName = dkUserName;
    }

    public String getDkMemberName() {
        return dkMemberName;
    }

    public void setDkMemberName(String dkMemberName) {
        this.dkMemberName = dkMemberName;
    }
}
