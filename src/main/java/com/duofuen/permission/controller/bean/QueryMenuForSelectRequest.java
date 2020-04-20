package com.duofuen.permission.controller.bean;

public class QueryMenuForSelectRequest {
    private int projectId;
    private int roleId;

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}
