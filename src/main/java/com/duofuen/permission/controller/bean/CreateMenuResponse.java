package com.duofuen.permission.controller.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

public class CreateMenuResponse extends BaseResponse<CreateMenuResponse.Data>{
    public CreateMenuResponse() {
        super();
        this.data = new Data();
    }

    public class Data {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Integer menuId;

        public Integer getMenuId() {
            return menuId;
        }

        public void setMenuId(Integer menuId) {
            this.menuId = menuId;
        }
    }
}
