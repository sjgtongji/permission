package com.duofuen.permission.controller.bean;

import java.util.List;

public class BatchDeleteUserRequest {
    private List<Integer> ids;

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
}
