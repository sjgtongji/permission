package com.duofuen.permission.common;

public enum ErrorNum {
    SUCCESS(200, "success"),
    FAIL(201 , "查询失败"),
    EMPTY_APPID(202, "appId不能为空"),
    EMTPY_APPSECRET(203 , "appSecret不能为空"),
    INVALID_APPID_OR_APPSECRET(204 , "appId或者appSecret无效"),
    INVALID_PARAM_NAME(205 , "name参数无效"),
    INVALID_PARAM_ROLE_ID(206, "roleId参数无效"),
    INVALID_PARAM_USERNAME(207, "userName参数无效"),
    INVALID_PARAM_PWD(208, "password参数无效"),
    INVALID_PARAM_PJO_ID(209 , "projectId参数无效"),
    INVALID_PARAM_PJO_ID_OR_ROLE_ID(210 , "roleId或者projectId参数无效"),
    INVALID_PARAM_URL(211 , "url参数无效"),
    INVALID_PARAM_PARENT_ID(212 , "parentId参数无效"),
    INVALID_PARAM_USER_ID(213 , "userId参数无效"),
    INVALID_PARAM_COMP(214 , "component参数无效"),
    INVALID_PARAM_MENU_ID(215 , "menuId参数无效"),
    UNKNOWN(500, "未知错误");
    private int code;
    private String msg;

    ErrorNum(int code , String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
