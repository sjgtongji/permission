package com.duofuen.permission.controller.bean;

import com.duofuen.permission.domain.entity.Menu;
import com.duofuen.permission.domain.entity.Project;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

public class QueryMenuForSelectResponse extends BaseResponse<QueryMenuForSelectResponse.Data>{
    public QueryMenuForSelectResponse() {
        super();
        this.data = new QueryMenuForSelectResponse.Data();
    }

    public class Data {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private List<String> checkedKeys = new ArrayList<>();

        private List<MenuData> data = new ArrayList<>();

        public List<String> getCheckedKeys() {
            return checkedKeys;
        }

        public void setCheckedKeys(List<String> checkedKeys) {
            this.checkedKeys = checkedKeys;
        }

        public List<MenuData> getData() {
            return data;
        }

        public void setData(List<MenuData> data) {
            this.data = data;
        }
    }

    public class MenuData{
        private String title;
        private String key;
        private List<MenuData> children;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<MenuData> getChildren() {
            return children;
        }

        public void setChildren(List<MenuData> children) {
            this.children = children;
        }
    }

    public void constructResponseMenu(List<Menu> menus){
        List<MenuData> data = new ArrayList<>();
        for(Menu menu : menus){
            data.add(constructMenuData(menu));
        }
        this.data.setData(data);
    }

    public MenuData constructMenuData(Menu menu){
        MenuData data = new MenuData();
        data.setKey(menu.getId().toString());
        data.setTitle(menu.getName());
        if(menu.getChildren() != null && menu.getChildren().size() > 0){
            List<MenuData> children = new ArrayList<>();
            for(Menu menu1 : menu.getChildren()){
                children.add(constructMenuData(menu1));
            }
            data.setChildren(children);
        }
        return data;
    }

    public void constructResponseCheckKeys(List<Menu> menus){
        List<String> checkedKeys = new ArrayList<>();
        for(Menu m : menus){
            checkedKeys.add(m.getId().toString());
        }
        getData().setCheckedKeys(checkedKeys);
    }
}
