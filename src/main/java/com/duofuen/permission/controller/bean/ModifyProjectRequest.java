package com.duofuen.permission.controller.bean;

import com.duofuen.permission.common.Constant;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;

public class ModifyProjectRequest {
    private int sort = Constant.SORT;
    private boolean valid = true;
    private int version = Constant.VERSION;
    private String name;
    private String companyName;
    private String phone;
    private String email;
    private int id = -1;

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
