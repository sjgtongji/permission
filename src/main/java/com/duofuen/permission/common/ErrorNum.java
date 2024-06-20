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
    INVALID_PARAM_ROLE_CODE_EXIST(216 , "code参数已存在"),
    INVALID_PARAM_ROLE_CODE_EMPTY(217 , "code参数无效"),
    INVALID_PARAM_PJO_ID_OR_MENU_ID(218, "menuId或者projectId参数无效"),
    INVALID_PARAM_ADDRESS(219 , "address参数无效"),
    INVALID_PARAM_PHONE(220 , "phone参数无效"),
    INVALID_PARAM_TOKEN(221 , "token参数无效"),
    INVALID_PARAM_HEAD(222 , "head参数无效"),
    INVALID_PARAM_PERMISSION(223 , "权限错误"),
    INVALID_PARAM_PATH(224, "path参数无效"),
    INVALID_PARAM_STORE_ID(225, "门店参数无效"),
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
