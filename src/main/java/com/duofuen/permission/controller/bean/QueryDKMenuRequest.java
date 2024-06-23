package com.duofuen.permission.controller.bean;

import com.duofuen.permission.domain.entity.DKStore;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class QueryDKMenuRequest {

    private String name = "";

    private String path = "";

    private Integer permission;

    private String dkStoreName;

    private int current = 1;
    private int pageSize = 20;
    private String createTime;
    private String sorter = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getPermission() {
        return permission;
    }

    public void setPermission(Integer permission) {
        this.permission = permission;
    }

    public String getDkStoreName() {
        return dkStoreName;
    }

    public void setDkStoreName(String dkStoreName) {
        this.dkStoreName = dkStoreName;
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

    public String getSorter() {
        return sorter;
    }

    public void setSorter(String sorter) {
        this.sorter = sorter;
    }
}
