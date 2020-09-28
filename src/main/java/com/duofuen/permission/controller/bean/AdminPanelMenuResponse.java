package com.duofuen.permission.controller.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class AdminPanelMenuResponse extends BaseResponse<AdminPanelMenuResponse.Data>{
    public AdminPanelMenuResponse() {
        super();
        this.data = new AdminPanelMenuResponse.Data();
    }
    public class Data {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private List<AdminPanelMenu> menus;

        public List<AdminPanelMenu> getMenus() {
            return menus;
        }

        public void setMenus(List<AdminPanelMenu> menus) {
            this.menus = menus;
        }
    }

    public static class AdminPanelMenu{
        public AdminPanelMenu(String path, String name, String icon) {
            this.path = path;
            this.name = name;
            this.icon = icon;
        }

        private String path;
        private String name;
        private String icon;
        private List<AdminPanelMenu> children;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public List<AdminPanelMenu> getChildren() {
            return children;
        }

        public void setChildren(List<AdminPanelMenu> children) {
            this.children = children;
        }
    }
}
